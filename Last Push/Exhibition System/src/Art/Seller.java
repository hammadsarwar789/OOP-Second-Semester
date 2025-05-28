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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Seller {
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;
    private final String sellerEmail;
    private TableView<ArtPiece> artTable;
    private File selectedImageFile;

    public Seller(ArtExhibitionManager manager, UserManager userManager, Stage stage, String sellerEmail) {
        this.manager = manager;
        this.userManager = userManager;
        this.stage = stage;
        this.sellerEmail = sellerEmail;
    }

    public void show() {
        stage.setTitle("Art Exhibition System - Seller Dashboard");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e6f0fa, #ffffff);");

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("Seller Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setEffect(new DropShadow(5, Color.web("rgba(0,0,0,0.3)")));
        header.getChildren().add(titleLabel);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        Tab viewTab = new Tab("View Art");
        viewTab.setClosable(false);
        viewTab.setContent(createViewTab());

        Tab addTab = new Tab("Add Art");
        addTab.setClosable(false);
        addTab.setContent(createAddTab());

        Tab updateTab = new Tab("Update Art");
        updateTab.setClosable(false);
        updateTab.setContent(createUpdateTab());

        Tab deleteTab = new Tab("Delete Art");
        deleteTab.setClosable(false);
        deleteTab.setContent(createDeleteTab());

        tabPane.getTabs().addAll(viewTab, addTab, updateTab, deleteTab);

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

    private VBox createViewTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        artTable = new TableView<>();
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
        artTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol, buyerCol, imageCol);
        updateArtTableItems();
        artTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        content.getChildren().addAll(artTable, messageLabel);
        return content;
    }

    private void updateArtTableItems() {
        artTable.setItems(javafx.collections.FXCollections.observableArrayList(
                manager.getAllArtPieces().stream()
                        .filter(art -> art.getSellerEmail() != null && art.getSellerEmail().equalsIgnoreCase(sellerEmail))
                        .toList()
        ));
    }

    private void showImageDialog(ArtPiece artPiece) {
        if (artPiece.getImagePath() == null || artPiece.getImagePath().isEmpty()) {
            System.out.println("No image path for Art ID: " + artPiece.getId());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Image");
            alert.setHeaderText(null);
            alert.setContentText("No image available for this art piece.");
            alert.showAndWait();
            return;
        }

        File imageFile = new File(artPiece.getImagePath());
        if (!imageFile.exists()) {
            System.out.println("Image file not found: " + artPiece.getImagePath());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Image Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The image file could not be found at: " + artPiece.getImagePath());
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

    private VBox createAddTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        TextField artistNameField = new TextField();
        artistNameField.setPromptText("Artist Name");
        artistNameField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        TextField bioField = new TextField();
        bioField.setPromptText("Artist Bio");
        bioField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Painting", "Sculpture", "Digital");
        typeCombo.setPromptText("Select Type");
        typeCombo.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        Button chooseImageButton = new Button("Choose Image");
        styleButton(chooseImageButton);
        Label imageLabel = new Label("No image selected");
        imageLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #6c757d;");

        chooseImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Art Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            selectedImageFile = fileChooser.showOpenDialog(stage);
            if (selectedImageFile != null) {
                imageLabel.setText(selectedImageFile.getName());
            } else {
                imageLabel.setText("No image selected");
            }
        });

        Button addButton = new Button("Add Art Piece");
        styleButton(addButton);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Artist Name:"), 0, 1);
        grid.add(artistNameField, 1, 1);
        grid.add(new Label("Artist Bio:"), 0, 2);
        grid.add(bioField, 1, 2);
        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeCombo, 1, 3);
        grid.add(new Label("Price:"), 0, 4);
        grid.add(priceField, 1, 4);
        grid.add(new Label("Image:"), 0, 5);
        grid.add(chooseImageButton, 1, 5);
        grid.add(imageLabel, 1, 6);
        grid.add(addButton, 1, 7);

        addButton.setOnAction(e -> {
            try {
                String title = titleField.getText().trim();
                String artistName = artistNameField.getText().trim();
                String bio = bioField.getText().trim();
                String type = typeCombo.getValue();
                double price = Double.parseDouble(priceField.getText().trim());

                if (title.isEmpty() || artistName.isEmpty() || bio.isEmpty() || type == null || price < 0) {
                    messageLabel.setText("Please fill all fields correctly.");
                    return;
                }

                String id = manager.generateNextArtId();
                String imagePath = null;
                if (selectedImageFile != null) {
                    File destDir = new File("art_images");
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                    File destFile = new File(destDir, "art_" + id + "_" + selectedImageFile.getName());
                    Files.copy(selectedImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    imagePath = "art_images" + File.separator + "art_" + id + "_" + selectedImageFile.getName();
                    System.out.println("Copied image to: " + destFile.getPath());
                }

                Artist artist = new Artist(artistName, bio);
                ArtPiece artPiece = new ArtPiece(id, title, artist, type, price, null, sellerEmail, imagePath);
                manager.addArtPiece(artPiece);
                messageLabel.setText("Art piece added successfully!");

                updateArtTableItems();

                try {
                    QRCodeGenerator.generateQRCode(artPiece);
                    File qrFile = new File("ArtQRCode_" + artPiece.getId() + ".png");
                    if (qrFile.exists()) {
                        Image qrImage = new Image(qrFile.toURI().toString());
                        ImageView qrView = new ImageView(qrImage);
                        qrView.setFitWidth(200);
                        qrView.setFitHeight(200);

                        Alert qrAlert = new Alert(Alert.AlertType.INFORMATION);
                        qrAlert.setTitle("QR Code Generated");
                        qrAlert.setHeaderText("QR Code for Art ID: " + artPiece.getId());
                        qrAlert.getDialogPane().setContent(qrView);
                        qrAlert.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

                        qrAlert.getDialogPane().setOpacity(0);
                        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), qrAlert.getDialogPane());
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        qrAlert.setOnShown(event -> fadeIn.play());

                        qrAlert.showAndWait();
                    } else {
                        messageLabel.setText("Art piece added, but QR code file not found.");
                    }
                } catch (Exception ex) {
                    messageLabel.setText("Art piece added, but failed to generate QR code: " + ex.getMessage());
                }

                titleField.clear();
                artistNameField.clear();
                bioField.clear();
                typeCombo.setValue(null);
                priceField.clear();
                selectedImageFile = null;
                imageLabel.setText("No image selected");
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid price format.");
            } catch (Exception ex) {
                messageLabel.setText("Error saving image: " + ex.getMessage());
            }
        });

        content.getChildren().addAll(grid, messageLabel);
        return content;
    }

    private VBox createUpdateTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        HBox searchBox = new HBox(10);
        TextField idField = new TextField();
        idField.setPromptText("Enter Art ID");
        idField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button loadButton = new Button("Load Art");
        styleButton(loadButton);
        searchBox.getChildren().addAll(idField, loadButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setDisable(true);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        TextField artistNameField = new TextField();
        artistNameField.setPromptText("Artist Name");
        artistNameField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        TextField bioField = new TextField();
        bioField.setPromptText("Artist Bio");
        bioField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Painting", "Sculpture", "Digital");
        typeCombo.setPromptText("Select Type");
        typeCombo.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        Button updateButton = new Button("Update Art");
        styleButton(updateButton);

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Artist Name:"), 0, 1);
        grid.add(artistNameField, 1, 1);
        grid.add(new Label("Artist Bio:"), 0, 2);
        grid.add(bioField, 1, 2);
        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeCombo, 1, 3);
        grid.add(new Label("Price:"), 0, 4);
        grid.add(priceField, 1, 4);
        grid.add(updateButton, 1, 5);

        loadButton.setOnAction(e -> {
            String id = idField.getText().trim();
            ArtPiece artPiece = manager.getArtPieceById(id);
            if (artPiece == null || !artPiece.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                messageLabel.setText("Art piece not found or not owned by you.");
                grid.setDisable(true);
                return;
            }
            titleField.setText(artPiece.getTitle());
            artistNameField.setText(artPiece.getArtist().getName());
            bioField.setText(artPiece.getArtist().getBio());
            typeCombo.setValue(artPiece.getType());
            priceField.setText(String.valueOf(artPiece.getPrice()));
            grid.setDisable(false);
            messageLabel.setText("Art piece loaded. Update fields and click Update.");
        });

        updateButton.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                String artistName = artistNameField.getText().trim();
                String bio = bioField.getText().trim();
                String type = typeCombo.getValue();
                double price = Double.parseDouble(priceField.getText().trim());

                if (title.isEmpty() || artistName.isEmpty() || bio.isEmpty() || type == null || price < 0) {
                    messageLabel.setText("Please fill all fields correctly.");
                    return;
                }

                ArtPiece artPiece = manager.getArtPieceById(id);
                if (artPiece == null || !artPiece.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                    messageLabel.setText("Art piece not found or not owned by you.");
                    return;
                }

                Artist artist = new Artist(artistName, bio);
                manager.updateArtPiece(id, title, artist, type, price, artPiece.getBuyerEmail(), artPiece.getSellerEmail());
                messageLabel.setText("Art piece updated successfully!");

                updateArtTableItems();

                grid.setDisable(true);
                idField.clear();
                titleField.clear();
                artistNameField.clear();
                bioField.clear();
                typeCombo.setValue(null);
                priceField.clear();
            } catch (NumberFormatException ex) {
                messageLabel.setText("Invalid price format.");
            }
        });

        content.getChildren().addAll(searchBox, grid, messageLabel);
        return content;
    }

    private VBox createDeleteTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        HBox deleteBox = new HBox(10);
        TextField idField = new TextField();
        idField.setPromptText("Enter Art ID");
        idField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");
        Button deleteButton = new Button("Delete Art");
        styleButton(deleteButton);
        deleteBox.getChildren().addAll(idField, deleteButton);

        deleteButton.setOnAction(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                messageLabel.setText("Please enter an Art ID.");
                return;
            }
            ArtPiece artPiece = manager.getArtPieceById(id);
            if (artPiece == null || !artPiece.getSellerEmail().equalsIgnoreCase(sellerEmail)) {
                messageLabel.setText("Art piece not found or not owned by you.");
                return;
            }
            manager.deleteArtPiece(id);
            messageLabel.setText("Art piece deleted successfully!");
            updateArtTableItems();
            idField.clear();
        });

        content.getChildren().addAll(deleteBox, messageLabel);
        return content;
    }
}