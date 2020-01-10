package nl.ou.testar.OCR;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.leptonica.*;

import java.io.File;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;


public class OcrController {
    private TessBaseAPI api;

    public void Create() {
        api = new TessBaseAPI();
        if (api.Init("C:\\Users\\Tycho\\AppData\\Local\\Tesseract-OCR\\tessdata", "eng") != 0) {
            System.err.println("Could not initialize tesseract.");
        }
        System.out.println("Created OCR");
    }

    public void Destroy() {
        // Destroy used object and release memory
        api.End();
        System.out.println("Destroyed OCR");
    }

    public void ProcessPNG(String path) {
        File image = new File(path);
        if (image.exists()) {
            PIX screenshot = pixRead(path);
            api.SetImage(screenshot);

            BytePointer result = api.GetUTF8Text();

            if (result != null) {
                System.out.printf("OCR HAS FOUND: %s\n", result.getString());
                result.deallocate();
            } else {
                System.out.printf("OCR failed to parse image: %s\n", path);
            }

            pixDestroy(screenshot);
        } else {
            System.out.printf("OCR: File not found: %s\n", path);
        }
    }
}
