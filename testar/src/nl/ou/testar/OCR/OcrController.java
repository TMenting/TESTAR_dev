package nl.ou.testar.OCR;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.tesseract.ResultIterator;
import org.bytedeco.tesseract.TessBaseAPI;
import org.bytedeco.leptonica.*;
import org.bytedeco.tesseract.ETEXT_DESC;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;
import static org.bytedeco.tesseract.global.tesseract.RIL_TEXTLINE;
import static org.bytedeco.tesseract.global.tesseract.RIL_WORD;


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

    public List<String> ProcessPNG(String path) {
        File image = new File(path);
        List<String> ocrResult = new Vector<>();
        if (image.exists()) {
            PIX screenshot = pixRead(path);
            api.SetImage(screenshot);

            api.Recognize(new ETEXT_DESC());
            ResultIterator ri = api.GetIterator();

            int level = RIL_WORD;
            if (ri != null) {
                System.out.println("OCR has found:");

                do {
                    BytePointer word = ri.GetUTF8Text(level);
                    float conf = ri.Confidence(level);
                    int[] x1 = new int[1];
                    int[] x2 = new int[1];
                    int[] y1 = new int[1];
                    int[] y2 = new int[1];

                    ri.BoundingBox(level, x1, y1, x2, y2);
                    System.out.printf("Word: '%s';  \tconf: %.2f; BoundingBox: %d,%d,%d,%d;\n",
                            word.getString(), conf, x1[0], y1[0], x2[0], y2[0]);

                    ocrResult.add(word.getString());
                    word.deallocate();
                } while (ri.Next(level));
            }

            else {
                System.out.printf("OCR failed to parse image: %s\n", path);
            }

            pixDestroy(screenshot);
        } else {
            System.out.printf("OCR: File not found: %s\n", path);
        }
        return ocrResult;
    }
}
