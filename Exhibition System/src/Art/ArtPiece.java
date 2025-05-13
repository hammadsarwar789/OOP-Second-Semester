package Art;

public class ArtPiece {
    private String id;
    private String title;
    private Artist artist;
    private String type;
    private double price;

    public ArtPiece(String id, String title, Artist artist, String type, double price) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.type = type;
        this.price = price;
    }

    public String getInfo() {
        return "ID: " + id + "\nTitle: " + title + "\nArtist: " + artist.getName() + 
               "\nType: " + type + "\nPrice: $" + String.format("%.2f", price);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public Artist getArtist() { return artist; }
}
