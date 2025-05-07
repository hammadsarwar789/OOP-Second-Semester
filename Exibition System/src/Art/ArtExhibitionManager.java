package Art;
import java.util.ArrayList;
import java.util.List;

public class ArtExhibitionManager {
    private List<ArtPiece> artPieces = new ArrayList<>();
    private char currentLetter = 'A';
    private int currentNumber = 1;

    public String generateNextArtId() {
        String id = String.format("%c%02d", currentLetter, currentNumber);
        
        currentNumber++;
        if (currentNumber > 9) {
            currentNumber = 1;
            currentLetter++;
        }
        
        return id;
    }

    public void addArtPiece(ArtPiece artPiece) {
        artPieces.add(artPiece);
    }

    public List<ArtPiece> getAllArtPieces() {
        return artPieces;
    }

    public void deleteArtPiece(String id) {
        artPieces.removeIf(art -> art.getId().equals(id));
    }

    public void updateArtPiece(String id, String title, Artist artist, String type, double price) {
        for (int i = 0; i < artPieces.size(); i++) {
            if (artPieces.get(i).getId().equals(id)) {
                artPieces.set(i, new ArtPiece(id, title, artist, type, price));
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
}
