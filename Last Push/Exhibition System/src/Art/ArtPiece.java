package Art;

public class ArtPiece {
    private String id;
    private String title;
    private Artist artist;
    private String type;
    private double price;
    private String buyerEmail;
    private String sellerEmail;

    public ArtPiece(String id, String title, Artist artist, String type, double price) {
        this(id, title, artist, type, price, null, null);
    }

    public ArtPiece(String id, String title, Artist artist, String type, double price, String buyerEmail, String sellerEmail) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.type = type;
        this.price = price;
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
    }

    public String getInfo() {
        return "ID: " + id + "\nTitle: " + title + "\nArtist: " + artist.getName() +
                "\nType: " + type + "\nPrice: $" + String.format("%.2f", price) +
                (buyerEmail != null ? ("\nBought by: " + buyerEmail) : "") +
                (sellerEmail != null ? ("\nSold by: " + sellerEmail) : "");
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public Artist getArtist() { return artist; }
    public String getBuyerEmail() { return buyerEmail; }
    public String getSellerEmail() { return sellerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }
}