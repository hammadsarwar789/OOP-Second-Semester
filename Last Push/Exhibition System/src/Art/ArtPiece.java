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
    private String bankName;      // New field for bank name
    private String accountNumber; // New field for account number

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

    public String getId() { return id; }
    public String getTitle() { return title; }
    public Artist getArtist() { return artist; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public String getBuyerEmail() { return buyerEmail; }
    public String getSellerEmail() { return sellerEmail; }
    public String getImagePath() { return imagePath; }
    public String getBankName() { return bankName; }      // New getter for bank name
    public String getAccountNumber() { return accountNumber; } // New getter for account number

    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setBankName(String bankName) { this.bankName = bankName; }      // New setter for bank name
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; } // New setter for account number

    public String getInfo() {
        return String.format("ID: %s, Title: %s, Artist: %s, Type: %s, Price: $%.2f, Buyer: %s, Seller: %s, Bank: %s, Account: %s",
                id, title, artist.getName(), type, price,
                buyerEmail != null ? buyerEmail : "None",
                sellerEmail != null ? sellerEmail : "None",
                bankName != null ? bankName : "N/A",         // Include bank name
                accountNumber != null ? "****" + accountNumber.substring(accountNumber.length() - 4) : "N/A"); // Mask account number
    }
}