package Art;

import java.io.Serializable;

public class ArtPiece implements Serializable {
    private String id;
    private String title;
    private Artist artist;
    private String type;
    private double price;
    private String buyerEmail;
    private String sellerEmail;
    private String imagePath;

    public ArtPiece(String id, String title, Artist artist, String type, double price, String buyerEmail, String sellerEmail) {
        this(id, title, artist, type, price, buyerEmail, sellerEmail, null);
    }

    public ArtPiece(String id, String title, Artist artist, String type, double price, String buyerEmail, String sellerEmail, String imagePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.type = type;
        this.price = price;
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public Artist getArtist() { return artist; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public String getBuyerEmail() { return buyerEmail; }
    public String getSellerEmail() { return sellerEmail; }
    public String getImagePath() { return imagePath; }

    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    // New method to return formatted art piece details
    public String getInfo() {
        return String.format("ID: %s, Title: %s, Artist: %s, Type: %s, Price: $%.2f, Buyer: %s, Seller: %s",
                id, title, artist.getName(), type, price,
                buyerEmail != null ? buyerEmail : "None",
                sellerEmail != null ? sellerEmail : "None");
    }
}