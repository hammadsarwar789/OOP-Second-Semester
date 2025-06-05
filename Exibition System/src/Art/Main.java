import java.util.*;
import java.io.*;
import com.google.zxing.*;
import com.google.zxing.qrcode.*;
import com.google.zxing.common.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class Main {
    // ...existing code...

    private static void addArtPiece(ArtExhibitionManager manager, Scanner scanner) {
        // ...existing code...
        // After successfully adding the art piece:
        manager.addArtPiece(newArt);
        System.out.println("Art piece added successfully!");

        // Generate QR code for the added art
        try {
            generateQRCode(newArt);
            System.out.println("QR code generated for Art ID: " + newArt.getId());
        } catch (Exception e) {
            System.out.println("Failed to generate QR code: " + e.getMessage());
        }
    }

    private static void generateQRCode(ArtPiece art) throws Exception {
        String qrContent = art.getInfo();
        int width = 300;
        int height = 300;
        String fileType = "png";
        String filePath = "ArtQRCode_" + art.getId() + ".png";

        BitMatrix matrix = new QRCodeWriter().encode(qrContent, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToPath(matrix, fileType, new File(filePath).toPath());
    }

    // ...existing code...
}
