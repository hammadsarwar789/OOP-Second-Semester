package Art;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArtExhibitionManager {
    private List<ArtPiece> artPieces = new ArrayList<>();
    private char currentLetter = 'A';
    private int currentNumber = 1;
    private static final String FILE_PATH = "art_pieces.txt";

    public String generateNextArtId() {
        String id = String.format("%c%02d", currentLetter, currentNumber);
        currentNumber++;
        if (currentNumber > 99) {
            currentNumber = 1;
            currentLetter++;
        }
        return id;
    }

    public void addArtPiece(ArtPiece artPiece) {
        artPieces.add(artPiece);
        saveToFile();
    }

    public List<ArtPiece> getAllArtPieces() {
        return new ArrayList<>(artPieces);
    }

    public void deleteArtPiece(String id) {
        artPieces.removeIf(art -> art.getId().equals(id));
        saveToFile();
    }

    public void updateArtPiece(String id, String title, Artist artist, String type, double price, String buyerEmail, String sellerEmail) {
        for (int i = 0; i < artPieces.size(); i++) {
            if (artPieces.get(i).getId().equals(id)) {
                artPieces.set(i, new ArtPiece(id, title, artist, type, price, buyerEmail, sellerEmail));
                saveToFile();
                break;
            }
        }
    }

    public ArtPiece getArtPieceById(String id) {
        return artPieces.stream()
                .filter(art -> art.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (ArtPiece art : artPieces) {
                writer.write(String.join(",",
                        art.getId(),
                        art.getTitle(),
                        art.getArtist().getName(),
                        art.getArtist().getBio(),
                        art.getType(),
                        String.valueOf(art.getPrice()),
                        art.getBuyerEmail() != null ? art.getBuyerEmail() : "",
                        art.getSellerEmail() != null ? art.getSellerEmail() : ""
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving art pieces: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        artPieces.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    String id = parts[0];
                    String title = parts[1];
                    String artistName = parts[2];
                    String artistBio = parts[3];
                    String type = parts[4];
                    double price;
                    try {
                        price = Double.parseDouble(parts[5]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price format for art piece: " + id);
                        continue;
                    }
                    String buyerEmail = parts.length > 6 && !parts[6].isEmpty() ? parts[6] : null;
                    String sellerEmail = parts.length > 7 && !parts[7].isEmpty() ? parts[7] : null;

                    Artist artist = new Artist(artistName, artistBio);
                    ArtPiece artPiece = new ArtPiece(id, title, artist, type, price, buyerEmail, sellerEmail);
                    artPieces.add(artPiece);

                    char letter = id.charAt(0);
                    int number = Integer.parseInt(id.substring(1));
                    if (letter > currentLetter || (letter == currentLetter && number >= currentNumber)) {
                        currentLetter = letter;
                        currentNumber = number + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading art pieces: " + e.getMessage());
        }
    }
}