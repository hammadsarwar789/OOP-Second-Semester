package Art;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;

public class QRCodeGenerator {

    public static void generateQRCode(String data, String filePath) throws Exception {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Ensure the directory exists
            Path path = Path.of(filePath).getParent();
            if (path != null && !Files.exists(path)) {
                Files.createDirectories(path);
            }

            MatrixToImageWriter.writeToPath(matrix, "PNG", Path.of(filePath));
        } catch (Exception e) {
            throw new Exception("Failed to generate QR code: " + e.getMessage(), e);
        }
    }
}
