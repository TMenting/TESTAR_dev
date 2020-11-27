package nl.ou.testar.visualvalidation;

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

    @Override
    public int compareTo(VisualValidationSettings other) {
        int res = -1;
        if ((enabled.equals(other.enabled)) &&
                (ocrConfiguration.compareTo(other.ocrConfiguration) == 0)) {
            res = 0;
        }
        return res;
    }

    @Override
    public String toString() {
        return "VisualValidationSettings{" +
                "enabled=" + enabled +
                '}';
    }

    public static VisualValidationSettings CreateDefault() {
        VisualValidationSettings DefaultInstance = new VisualValidationSettings();
        DefaultInstance.enabled = false;
        DefaultInstance.ocrConfiguration = OcrConfiguration.CreateDefault();
        return DefaultInstance;
    }
}
