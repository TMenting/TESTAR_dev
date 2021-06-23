package nl.ou.testar.visualvalidation;

import nl.ou.testar.visualvalidation.matcher.MatcherConfiguration;
import nl.ou.testar.visualvalidation.ocr.OcrConfiguration;
import org.testar.settings.ExtendedSettingBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VisualValidationSettings extends ExtendedSettingBase<VisualValidationSettings> {
    public Boolean enabled;
    public OcrConfiguration ocrConfiguration;
    public MatcherConfiguration matcherConfiguration;
    // The selected protocol will be set automatically when we initialize TESTAR.
    public String protocol;

    public static VisualValidationSettings CreateDefault() {
        VisualValidationSettings defaultInstance = new VisualValidationSettings();
        defaultInstance.enabled = true;
        defaultInstance.ocrConfiguration = OcrConfiguration.CreateDefault();
        defaultInstance.matcherConfiguration = MatcherConfiguration.CreateDefault();
        return defaultInstance;
    }

    @Override
    public int compareTo(VisualValidationSettings other) {
        int res = -1;
        if ((enabled.equals(other.enabled)) &&
                (ocrConfiguration.compareTo(other.ocrConfiguration) == 0) &&
                (matcherConfiguration.compareTo(other.matcherConfiguration) == 0)
        ) {
            res = 0;
        }
        return res;
    }

    @Override
    public String toString() {
        return "VisualValidationSettings{" +
                "enabled=" + enabled +
                ", ocr=" + ocrConfiguration +
                ", matcher=" + matcherConfiguration +
                '}';
    }
}
