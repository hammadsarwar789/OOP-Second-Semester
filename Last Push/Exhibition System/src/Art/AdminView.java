package Art;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminView {
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;

    public AdminView(ArtExhibitionManager manager, UserManager userManager, Stage stage) {
        this.manager = manager;
        this.userManager = userManager;
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Art Exhibition System - Admin Dashboard");

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f0f4f8;");

        // Top: Title
        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox topBox = new HBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));
        layout.setTop(topBox);

        // Center: Menu
        VBox menuBox = new VBox(10);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(20));

        Button removeBuyerButton = new Button("Remove Buyer");
        Button removeSellerButton = new Button("Remove Seller");
        Button removeArtButton = new Button("Remove Art Piece");
        Button viewUsersButton = new Button("View All Users");
        Button viewArtButton = new Button("View All Art Pieces");
        Button searchArtButton = new Button("Search Art by ID");
        Button viewBuyerPurchasesButton = new Button("View Buyer's Purchases");
        Button viewSellerSalesButton = new Button("View Seller's Sales");
        Button logoutButton = new Button("Logout");

        styleButton(removeBuyerButton);
        styleButton(removeSellerButton);
        styleButton(removeArtButton);
        styleButton(viewUsersButton);
        styleButton(viewArtButton);
        styleButton(searchArtButton);
        styleButton(viewBuyerPurchasesButton);
        styleButton(viewSellerSalesButton);
        styleButton(logoutButton);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");

        removeBuyerButton.setOnAction(e -> removeUser("buyer", messageLabel));
        removeSellerButton.setOnAction(e -> removeUser("seller", messageLabel));
        removeArtButton.setOnAction(e -> removeArtPiece(messageLabel));
        viewUsersButton.setOnAction(e -> viewAllUsers(messageLabel));
        viewArtButton.setOnAction(e -> viewAllArtPieces(messageLabel));
        searchArtButton.setOnAction(e -> searchArtById(messageLabel));
        viewBuyerPurchasesButton.setOnAction(e -> viewBuyerPurchases(messageLabel));
        viewSellerSalesButton.setOnAction(e -> viewSellerSales(messageLabel));
        logoutButton.setOnAction(e -> new LoginView(manager, stage).show());

        menuBox.getChildren().addAll(
                removeBuyerButton, removeSellerButton, removeArtButton,
                viewUsersButton, viewArtButton, searchArtButton,
                viewBuyerPurchasesButton, viewSellerSalesButton, logoutButton, messageLabel
        );

        layout.setCenter(menuBox);

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;");
        button.setMaxWidth(300);
    }

    private void removeUser(String role, Label messageLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove " + role);
        dialog.setHeaderText("Enter email of the " + role + " to remove:");
        dialog.setContentText("Email:");
        dialog.showAndWait().ifPresent(email -> {
            if (email.equalsIgnoreCase("admin@admin.com")) {
                messageLabel.setText("Cannot remove the admin account.");
                return;
            }
            User user = userManager.getUsers().get(email.toLowerCase());
            if (user != null && user.getRole().equalsIgnoreCase(role)) {
                userManager.removeUser(email);
                messageLabel.setText(role.substring(0, 1).toUpperCase() + role.substring(1) + " removed successfully.");
            } else {
                messageLabel.setText("No such " + role + " found with that email.");
            }
        });
    }

    private void removeArtPiece(Label messageLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Art Piece");
        dialog.setHeaderText("Enter Art ID to remove:");
        dialog.setContentText("Art ID:");
        dialog.showAndWait().ifPresent(id -> {
            ArtPiece artPiece = manager.getArtPieceById(id);
            if (artPiece != null) {
                manager.deleteArtPiece(id);
                messageLabel.setText("Art piece removed successfully.");
            } else {
                messageLabel.setText("No art piece found with that ID.");
            }
        });
    }

    private void viewAllUsers(Label messageLabel) {
        StringBuilder sb = new StringBuilder("Registered Users:\n");
        for (User user : userManager.getUsers().values()) {
            sb.append("Email: ").append(user.getEmail()).append(", Role: ").append(user.getRole()).append("\n");
        }
        showAlert("All Users", sb.toString());
    }

    private void viewAllArtPieces(Label messageLabel) {
        StringBuilder sb = new StringBuilder("All Art Pieces:\n");
        if (manager.getAllArtPieces().isEmpty()) {
            sb.append("No art pieces found.");
        } else {
            for (ArtPiece artPiece : manager.getAllArtPieces()) {
                sb.append(artPiece.getInfo()).append("\n--------------------\n");
            }
        }
        showAlert("All Art Pieces", sb.toString());
    }

    private void searchArtById(Label messageLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Art");
        dialog.setHeaderText("Enter Art ID to search:");
        dialog.setContentText("Art ID:");
        dialog.showAndWait().ifPresent(id -> {
            ArtPiece artPiece = manager.getArtPieceById(id);
            if (artPiece != null) {
                showAlert("Art Found", artPiece.getInfo());
            } else {
                messageLabel.setText("No art piece found with that ID.");
            }
        });
    }

    private void viewBuyerPurchases(Label messageLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Buyer's Purchases");
        dialog.setHeaderText("Enter buyer's email:");
        dialog.setContentText("Email:");
        dialog.showAndWait().ifPresent(buyerEmail -> {
            StringBuilder sb = new StringBuilder("Art pieces bought by " + buyerEmail + ":\n");
            boolean found = false;
            for (ArtPiece artPiece : manager.getAllArtPieces()) {
                if (artPiece.getBuyerEmail() != null && artPiece.getBuyerEmail().equalsIgnoreCase(buyerEmail)) {
                    sb.append(artPiece.getInfo()).append("\n--------------------\n");
                    found = true;
                }
            }
            if (!found) {
                sb.append("No purchases found for this buyer.");
            }
            showAlert("Buyer's Purchases", sb.toString());
        });
    }

    private void viewSellerSales(Label messageLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Seller's Sales");
        dialog.setHeaderText("Enter seller's email:");
        dialog.setContentText("Email:");
        dialog.showAndWait().ifPresent(sellerEmail -> {
            StringBuilder sb = new StringBuilder("Art pieces sold by " + sellerEmail + ":\n");
            boolean found = false;
            for (ArtPiece artPiece : manager.getAllArtPieces()) {
                if (artPiece.getSellerEmail() != null && artPiece.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                    sb.append(artPiece.getInfo()).append("\n--------------------\n");
                    found = true;
                }
            }
            if (!found) {
                sb.append("No sales found for this seller.");
            }
            showAlert("Seller's Sales", sb.toString());
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}