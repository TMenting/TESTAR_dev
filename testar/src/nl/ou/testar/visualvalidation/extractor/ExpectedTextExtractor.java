package nl.ou.testar.visualvalidation.extractor;


import org.apache.logging.log4j.Level;
import org.fruit.Util;
import org.fruit.alayer.Roles;
import org.fruit.alayer.Shape;
import org.fruit.alayer.Tag;
import org.fruit.alayer.TagsBase;
import org.fruit.alayer.Widget;
import org.testar.Logger;
import org.testar.settings.ExtendedSettingsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.fruit.alayer.Tags.*;
import static org.fruit.alayer.webdriver.enums.WdTags.WebTextContent;

public class ExpectedTextExtractor extends Thread implements TextExtractorInterface {
    private static final String TAG = "ExpectedTextExtractor";

    AtomicBoolean running = new AtomicBoolean(true);

    private final Boolean _threadSync = true;
    private org.fruit.alayer.State _state = null;
    private ExpectedTextCallback _callback = null;

    /**
     * A blacklist for {@link Widget}'s which should be ignored based on their {@code Role} because the don't contain
     * readable text. Optional when the value (which represents the ancestor path) is set, the {@link Widget} should
     * only be ignored when the ancestor path is equal with the {@link Widget} under investigation.
     */
    private final Map<String, String> _blacklist = new HashMap<>();

    /**
     * A lookup table which indicates based on the {@code Role} which {@link Tag} should be used to extract the text.
     */
    private final Map<String, String> _lookupTable = new HashMap<>();

    /**
     * Lookup table to map the name of the name of a {@link Tag}, to the actual {@link Tag}.
     * Holds all the available tag's which could hold text (String types, only). The key represents the {@link Tag} in String type, value
     */
    @SuppressWarnings("unchecked")
    private static final Map<String, Tag<String>> _tag = TagsBase.tagSet().stream()
            .filter(tag -> tag.type().equals(String.class))
            .collect(Collectors.toMap(Tag::name, tag -> (Tag<String>) tag));

    ExpectedTextExtractor() {
        WidgetTextConfiguration config = ExtendedSettingsFactory.createWidgetTextConfiguration();
        // Load the extractor configuration into a lookup table for quick access.
        config.widget.forEach(it -> {
            if (it.ignore) {
                _blacklist.put(it.role, it.ancestor);
            } else {
                _lookupTable.put(it.role, it.tag);
            }
        });

        setName(TAG);
        start();
    }

    @Override
    public void ExtractExpectedText(org.fruit.alayer.State state, ExpectedTextCallback callback) {
        synchronized (_threadSync) {
            _state = state;
            _callback = callback;
            Logger.log(Level.TRACE, TAG, "Queue new text extract.");
            _threadSync.notifyAll();
        }
    }

    @Override
    public void run() {
        while (running.get()) {
            synchronized (_threadSync) {
                try {
                    // Wait until we need to inspect a new image.
                    _threadSync.wait();
                    if (!running.get()) {
                        break;
                    }
                    extractText();

                } catch (InterruptedException e) {
                    // Happens if someone interrupts your thread.
                    Logger.log(Level.INFO, TAG, "Wait interrupted");
                    e.printStackTrace();
                }
            }
        }
    }

    private void extractText() {
        if (_state == null || _callback == null) {
            Logger.log(Level.ERROR, TAG, "Should not try to extract text on empty state/callback");
            return;
        }

        List<ExpectedElement> expectedElements = new ArrayList<>();

        for (Widget widget : _state) {
            String widgetRole = widget.get(Role).name();

            if (widgetIsIncluded(widget, widgetRole)) {
                String text = widget.get(getVisualTextTag(widgetRole), "");

                if (text != null && !text.isEmpty()) {
                    Shape dimension = widget.get(Shape, null);
                    int x1 = dimension != null ? (int) dimension.x() : 0;
                    int y1 = dimension != null ? (int) dimension.y() : 0;
                    int x2 = dimension != null ? (int) (dimension.width() + dimension.x()) : 0;
                    int y2 = dimension != null ? (int) (dimension.height() + dimension.y()) : 0;

                    expectedElements.add(new ExpectedElement(x1, y1, x2, y2, text));
                }
            } else {
                Logger.log(Level.DEBUG, TAG, "Widget {} ignored", widgetRole);
            }
        }

        _callback.ReportExtractedText(expectedElements);
    }

    private boolean widgetIsIncluded(Widget w, String role) {
        boolean containsReadableText = true;
        try {
            String ancestors = _blacklist.get(role);
            if (!ancestors.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                Util.ancestors(w).forEach(it -> sb.append(WidgetTextSetting.ANCESTOR_SEPARATOR).append(it.get(Role, Roles.Widget)));

                // Check if we should ignore this widget based on its ancestors.
                if (sb.toString().equals(ancestors)) {
                    containsReadableText = false;
                }
            }
        } catch (NullPointerException ignored) {

        }
        return containsReadableText;
    }

    private Tag<String> getVisualTextTag(String widgetRole) {
        // Check if we have specified a custom tag for this widget, if so convert the string identifier into the actual
        // Tag.
        // TODO TM: Should inject this based on the selected protocol?
        Tag<String> defaultTag = widgetRole.contains("Wd") ? WebTextContent : Title;
        return _tag.getOrDefault(_lookupTable.getOrDefault(widgetRole, ""), defaultTag);
    }

    @Override
    public void Destroy() {
        stopAndJoinThread();

        Logger.log(Level.DEBUG, TAG, "Extractor destroyed.");
    }

    private void stopAndJoinThread() {
        synchronized (_threadSync) {
            running.set(false);
            _threadSync.notifyAll();
        }

        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
