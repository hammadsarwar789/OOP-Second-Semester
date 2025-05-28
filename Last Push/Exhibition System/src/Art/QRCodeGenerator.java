package Art;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {
    public static void generateQRCode(ArtPiece artPiece) throws Exception {
        String qrContent = artPiece.getInfo();
        int width = 300;
        int height = 300;
        String fileType = "png";
        String fileName = "ArtQRCode_" + artPiece.getId() + ".png";
        Path path = FileSystems.getDefault().getPath(fileName);

        BitMatrix matrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToPath(matrix, fileType, path);
    }
}