package Art;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ArtExhibitionManager {
    private Map<String, ArtPiece> artPieces;
    private static final String ART_FILE = "art_pieces.txt";

    public ArtExhibitionManager() {
        artPieces = new HashMap<>();
        loadFromFile();
    }

    public void addArtPiece(ArtPiece artPiece) {
        artPieces.put(artPiece.getId(), artPiece);
        saveToFile();
    }

    public void updateArtPiece(String id, String title, Artist artist, String type, double price, String buyerEmail, String sellerEmail) {
        ArtPiece artPiece = artPieces.get(id);
        if (artPiece != null) {

            artPieces.put(id, new ArtPiece(id, title, artist, type, price, buyerEmail, sellerEmail, artPiece.getImagePath()));
            saveToFile();
        }
    }

    public void deleteArtPiece(String id) {
        ArtPiece artPiece = artPieces.get(id);
        if (artPiece != null) {

            if (artPiece.getImagePath() != null && !artPiece.getImagePath().isEmpty()) {
                File imageFile = new File(artPiece.getImagePath());
                if (imageFile.exists()) {
                    try {
                        imageFile.delete();
                        System.out.println("Deleted image file: " + artPiece.getImagePath());
                    } catch (Exception e) {
                        System.err.println("Failed to delete image file: " + artPiece.getImagePath());
                    }
                }
            }
            artPieces.remove(id);
            saveToFile();
        }
    }

    public ArtPiece getArtPieceById(String id) {
        return artPieces.get(id);
    }

    public List<ArtPiece> getAllArtPieces() {
        return new ArrayList<>(artPieces.values());
    }

    public String generateNextArtId() {
        int maxId = artPieces.keySet().stream()
                .filter(id -> id.startsWith("A"))
                .map(id -> Integer.parseInt(id.substring(1)))
                .max(Integer::compare)
                .orElse(0);
        return String.format("A%02d", maxId + 1);
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ART_FILE))) {
            for (ArtPiece artPiece : artPieces.values()) {
                String buyerEmail = artPiece.getBuyerEmail() != null ? artPiece.getBuyerEmail() : "";
                String sellerEmail = artPiece.getSellerEmail() != null ? artPiece.getSellerEmail() : "";
                String imagePath = artPiece.getImagePath() != null ? artPiece.getImagePath() : "";
                String line = String.format("%s,%s,%s,%s,%s,%.2f,%s,%s,%s",
                        artPiece.getId(),
                        artPiece.getTitle(),
                        artPiece.getArtist().getName(),
                        artPiece.getArtist().getBio(),
                        artPiece.getType(),
                        artPiece.getPrice(),
                        buyerEmail,
                        sellerEmail,
                        imagePath);
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Saved art pieces to " + ART_FILE);
        } catch (IOException e) {
            System.err.println("Error saving art pieces: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        artPieces.clear();
        File file = new File(ART_FILE);
        if (!file.exists()) {
            System.out.println("No art pieces file found, starting fresh.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 9) {
                    try {
                        String id = parts[0].trim();
                        String title = parts[1].trim();
                        String artistName = parts[2].trim();
                        String bio = parts[3].trim();
                        String type = parts[4].trim();
                        double price = Double.parseDouble(parts[5].trim());
                        String buyerEmail = parts[6].isEmpty() ? null : parts[6].trim();
                        String sellerEmail = parts[7].isEmpty() ? null : parts[7].trim();
                        String imagePath = parts[8].isEmpty() ? null : parts[8].trim();

                        Artist artist = new Artist(artistName, bio);
                        ArtPiece artPiece = new ArtPiece(id, title, artist, type, price, buyerEmail, sellerEmail, imagePath);
                        artPieces.put(id, artPiece);
                        System.out.println("Loaded art piece: " + id + ", imagePath: " + imagePath);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price format in line: " + line);
                    }
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading art pieces: " + e.getMessage());
        }
    }
}