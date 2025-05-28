package Art;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;

public class Admin {
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;
    private static final String ADMIN_EMAIL = "admin@admin.com";

    public Admin(ArtExhibitionManager manager, UserManager userManager, Stage stage) {
        this.manager = manager;
        this.userManager = userManager;
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Art Exhibition System - Admin Dashboard");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e6f0fa, #ffffff);");

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setEffect(new DropShadow(5, Color.web("rgba(0,0,0,0.3)")));
        header.getChildren().add(titleLabel);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        Tab usersTab = new Tab("Manage Users");
        usersTab.setClosable(false);
        usersTab.setContent(createUsersTab());

        Tab artTab = new Tab("Manage Art");
        artTab.setClosable(false);
        artTab.setContent(createArtTab());

        Tab purchasesTab = new Tab("Purchases");
        purchasesTab.setClosable(false);
        purchasesTab.setContent(createPurchasesTab());

        Tab salesTab = new Tab("Sales");
        salesTab.setClosable(false);
        salesTab.setContent(createSalesTab());

        tabPane.getTabs().addAll(usersTab, artTab, purchasesTab, salesTab);

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 1 0 0 0;");

        Button logoutButton = new Button("Logout");
        styleButton(logoutButton);
        logoutButton.setOnAction(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(500), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(event -> new LoginView(manager, stage).show());
            fade.play();
        });
        footer.getChildren().add(logoutButton);

        root.setTop(header);
        root.setCenter(tabPane);
        root.setBottom(footer);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 14;");
        button.setEffect(new DropShadow(3, Color.web("rgba(0,0,0,0.2)")));
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 14;");
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 14;");
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }

    private VBox createUsersTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        TableView<User> usersTable = new TableView<>();
        usersTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));
        usersTable.getColumns().addAll(emailCol, roleCol);
        usersTable.setItems(javafx.collections.FXCollections.observableArrayList(userManager.getUsers().values()));
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox removeBox = new HBox(10);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email to remove");
        emailField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button removeBuyerButton = new Button("Remove Buyer");
        Button removeSellerButton = new Button("Remove Seller");
        styleButton(removeBuyerButton);
        styleButton(removeSellerButton);
        removeBox.getChildren().addAll(emailField, removeBuyerButton, removeSellerButton);

        removeBuyerButton.setOnAction(e -> removeUser("buyer", emailField.getText(), messageLabel, usersTable));
        removeSellerButton.setOnAction(e -> removeUser("seller", emailField.getText(), messageLabel, usersTable));

        content.getChildren().addAll(usersTable, removeBox, messageLabel);
        return content;
    }

    private VBox createArtTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        TableView<ArtPiece> artTable = new TableView<>();
        artTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<ArtPiece, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        TableColumn<ArtPiece, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        TableColumn<ArtPiece, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist().getName()));
        TableColumn<ArtPiece, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        TableColumn<ArtPiece, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        TableColumn<ArtPiece, String> buyerCol = new TableColumn<>("Buyer");
        buyerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuyerEmail() != null ? data.getValue().getBuyerEmail() : ""));
        TableColumn<ArtPiece, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSellerEmail() != null ? data.getValue().getSellerEmail() : ""));
        TableColumn<ArtPiece, Void> imageCol = new TableColumn<>("Image");
        imageCol.setCellFactory(param -> new TableCell<>() {
            private final Button imageButton = new Button("View Image");

            {
                styleButton(imageButton);
                imageButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 12;");
                imageButton.setOnMouseEntered(e -> {
                    imageButton.setStyle("-fx-background-color: #138496; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 12;");
                    imageButton.setScaleX(1.05);
                    imageButton.setScaleY(1.05);
                });
                imageButton.setOnMouseExited(e -> {
                    imageButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 12;");
                    imageButton.setScaleX(1.0);
                    imageButton.setScaleY(1.0);
                });

                imageButton.setOnAction(e -> {
                    ArtPiece artPiece = getTableView().getItems().get(getIndex());
                    showImageDialog(artPiece);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getImagePath() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(imageButton);
                }
            }
        });
        artTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol, buyerCol, sellerCol, imageCol);
        artTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllArtPieces()));
        artTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox removeBox = new HBox(10);
        TextField artIdField = new TextField();
        artIdField.setPromptText("Enter Art ID to remove");
        artIdField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button removeArtButton = new Button("Remove Art");
        styleButton(removeArtButton);
        removeBox.getChildren().addAll(artIdField, removeArtButton);

        removeArtButton.setOnAction(e -> removeArtPiece(artIdField.getText(), messageLabel, artTable));

        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Search Art by ID");
        searchField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button searchButton = new Button("Search");
        styleButton(searchButton);
        searchBox.getChildren().addAll(searchField, searchButton);

        searchButton.setOnAction(e -> {
            String id = searchField.getText().trim();
            if (id.isEmpty()) {
                artTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllArtPieces()));
                messageLabel.setText("Displaying all art pieces.");
            } else {
                ArtPiece artPiece = manager.getArtPieceById(id);
                if (artPiece != null) {
                    artTable.setItems(javafx.collections.FXCollections.observableArrayList(artPiece));
                    messageLabel.setText("Art piece found.");
                } else {
                    messageLabel.setText("No art piece found with that ID.");
                    artTable.setItems(javafx.collections.FXCollections.observableArrayList());
                }
            }
        });

        content.getChildren().addAll(artTable, removeBox, searchBox, messageLabel);
        return content;
    }

    private VBox createPurchasesTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        TableView<ArtPiece> purchasesTable = new TableView<>();
        purchasesTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<ArtPiece, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        TableColumn<ArtPiece, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        TableColumn<ArtPiece, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist().getName()));
        TableColumn<ArtPiece, String> buyerCol = new TableColumn<>("Buyer");
        buyerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuyerEmail() != null ? data.getValue().getBuyerEmail() : ""));
        purchasesTable.getColumns().addAll(idCol, titleCol, artistCol, buyerCol);
        purchasesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox searchBox = new HBox(10);
        TextField buyerEmailField = new TextField();
        buyerEmailField.setPromptText("Enter buyer's email");
        buyerEmailField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button searchButton = new Button("View Purchases");
        styleButton(searchButton);
        searchBox.getChildren().addAll(buyerEmailField, searchButton);

        searchButton.setOnAction(e -> {
            String buyerEmail = buyerEmailField.getText().trim().toLowerCase();
            if (buyerEmail.isEmpty()) {
                messageLabel.setText("Please enter a buyer's email.");
                purchasesTable.setItems(javafx.collections.FXCollections.observableArrayList());
            } else {
                java.util.List<ArtPiece> purchases = manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() != null && art.getBuyerEmail().equalsIgnoreCase(buyerEmail))
                        .toList();
                purchasesTable.setItems(javafx.collections.FXCollections.observableArrayList(purchases));
                messageLabel.setText(purchases.isEmpty() ? "No purchases found for this buyer." : "Purchases loaded.");
            }
        });

        content.getChildren().addAll(purchasesTable, searchBox, messageLabel);
        return content;
    }

    private VBox createSalesTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        TableView<ArtPiece> salesTable = new TableView<>();
        salesTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<ArtPiece, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        TableColumn<ArtPiece, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        TableColumn<ArtPiece, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist().getName()));
        TableColumn<ArtPiece, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSellerEmail() != null ? data.getValue().getSellerEmail() : ""));
        salesTable.getColumns().addAll(idCol, titleCol, artistCol, sellerCol);
        salesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox searchBox = new HBox(10);
        TextField sellerEmailField = new TextField();
        sellerEmailField.setPromptText("Enter seller's email");
        sellerEmailField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button searchButton = new Button("View Sales");
        styleButton(searchButton);
        searchBox.getChildren().addAll(sellerEmailField, searchButton);

        searchButton.setOnAction(e -> {
            String sellerEmail = sellerEmailField.getText().trim().toLowerCase();
            if (sellerEmail.isEmpty()) {
                messageLabel.setText("Please enter a seller's email.");
                salesTable.setItems(javafx.collections.FXCollections.observableArrayList());
            } else {
                java.util.List<ArtPiece> sales = manager.getAllArtPieces().stream()
                        .filter(art -> art.getSellerEmail() != null && art.getSellerEmail().equalsIgnoreCase(sellerEmail))
                        .toList();
                salesTable.setItems(javafx.collections.FXCollections.observableArrayList(sales));
                messageLabel.setText(sales.isEmpty() ? "No sales found for this seller." : "Sales loaded.");
            }
        });

        content.getChildren().addAll(salesTable, searchBox, messageLabel);
        return content;
    }

    private void removeUser(String role, String email, Label messageLabel, TableView<User> usersTable) {
        if (email.isEmpty()) {
            messageLabel.setText("Please enter an email.");
            return;
        }
        if (email.equalsIgnoreCase(ADMIN_EMAIL)) {
            messageLabel.setText("Cannot remove the admin account.");
            return;
        }
        User user = userManager.getUsers().get(email.toLowerCase());
        if (user != null && user.getRole().equalsIgnoreCase(role)) {
            userManager.removeUser(email);
            usersTable.setItems(javafx.collections.FXCollections.observableArrayList(userManager.getUsers().values()));
            messageLabel.setText(role.substring(0, 1).toUpperCase() + role.substring(1) + " removed successfully.");
        } else {
            messageLabel.setText("No such " + role + " found with that email.");
        }
    }

    private void removeArtPiece(String id, Label messageLabel, TableView<ArtPiece> artTable) {
        if (id.isEmpty()) {
            messageLabel.setText("Please enter an Art ID.");
            return;
        }
        ArtPiece artPiece = manager.getArtPieceById(id);
        if (artPiece != null) {
            manager.deleteArtPiece(id);
            artTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllArtPieces()));
            messageLabel.setText("Art piece removed successfully.");
        } else {
            messageLabel.setText("No art piece found with that ID.");
        }
    }

    private void showImageDialog(ArtPiece artPiece) {
        if (artPiece.getImagePath() == null || artPiece.getImagePath().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Image");
            alert.setHeaderText(null);
            alert.setContentText("No image available for this art piece.");
            alert.showAndWait();
            return;
        }

        File imageFile = new File(artPiece.getImagePath());
        if (!imageFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Image Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The image file could not be found.");
            alert.showAndWait();
            return;
        }

        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Art Image");
        alert.setHeaderText("Image for Art ID: " + artPiece.getId());
        alert.getDialogPane().setContent(imageView);
        alert.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        alert.getDialogPane().setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), alert.getDialogPane());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        alert.setOnShown(event -> fadeIn.play());

        alert.showAndWait();
    }
}