package Art;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class Buyer {
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;
    private final String buyerEmail;
    private TableView<ArtPiece> purchasesTable; // Store TableView reference for My Purchases

    public Buyer(ArtExhibitionManager manager, UserManager userManager, Stage stage, String buyerEmail) {
        this.manager = manager;
        this.userManager = userManager;
        this.stage = stage;
        this.buyerEmail = buyerEmail;
    }

    public void show() {
        stage.setTitle("Art Exhibition System - Buyer Dashboard");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e6f0fa, #ffffff);");

        // Header
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("Buyer Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setEffect(new DropShadow(5, Color.web("rgba(0,0,0,0.3)")));
        header.getChildren().add(titleLabel);

        // TabPane for different functionalities
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        Tab galleryTab = new Tab("Art Gallery");
        galleryTab.setClosable(false);
        galleryTab.setContent(createGalleryTab());

        Tab purchasesTab = new Tab("My Purchases");
        purchasesTab.setClosable(false);
        purchasesTab.setContent(createPurchasesTab());

        tabPane.getTabs().addAll(galleryTab, purchasesTab);

        // Footer with Logout
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

        // Fade-in animation
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

    private VBox createGalleryTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        TableView<ArtPiece> artTable = new TableView<>();
        artTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<ArtPiece, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<ArtPiece, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<ArtPiece, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist().getName()));
        TableColumn<ArtPiece, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<ArtPiece, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<ArtPiece, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button purchaseButton = new Button("Purchase");

            {
                styleButton(purchaseButton);
                purchaseButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 12;");
                purchaseButton.setOnMouseEntered(e -> {
                    purchaseButton.setStyle("-fx-background-color: #218838; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 12;");
                    purchaseButton.setScaleX(1.05);
                    purchaseButton.setScaleY(1.05);
                });
                purchaseButton.setOnMouseExited(e -> {
                    purchaseButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 5; -fx-font-size: 12;");
                    purchaseButton.setScaleX(1.0);
                    purchaseButton.setScaleY(1.0);
                });

                purchaseButton.setOnAction(e -> {
                    ArtPiece artPiece = getTableView().getItems().get(getIndex());
                    if (artPiece.getBuyerEmail() == null) {
                        artPiece.setBuyerEmail(buyerEmail);
                        manager.saveToFile();
                        messageLabel.setText("Art piece purchased successfully!");
                        // Update My Purchases TableView
                        updatePurchasesTableItems();
                        // Show QR code
                        try {
                            File qrFile = new File("ArtQRCode_" + artPiece.getId() + ".png");
                            if (qrFile.exists()) {
                                Image qrImage = new Image(qrFile.toURI().toString());
                                ImageView qrView = new ImageView(qrImage);
                                qrView.setFitWidth(200);
                                qrView.setFitHeight(200);

                                Alert qrAlert = new Alert(Alert.AlertType.INFORMATION);
                                qrAlert.setTitle("QR Code");
                                qrAlert.setHeaderText("QR Code for Art ID: " + artPiece.getId());
                                qrAlert.getDialogPane().setContent(qrView);
                                qrAlert.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

                                qrAlert.getDialogPane().setOpacity(0);
                                FadeTransition fadeIn = new FadeTransition(Duration.millis(500), qrAlert.getDialogPane());
                                fadeIn.setFromValue(0.0);
                                fadeIn.setToValue(1.0);
                                qrAlert.setOnShown(event -> fadeIn.play());

                                qrAlert.showAndWait();
                            }
                        } catch (Exception ex) {
                            messageLabel.setText("Failed to display QR code: " + ex.getMessage());
                        }
                        // Refresh Gallery table
                        artTable.setItems(javafx.collections.FXCollections.observableArrayList(
                                manager.getAllArtPieces().stream()
                                        .filter(art -> art.getBuyerEmail() == null)
                                        .toList()
                        ));
                    } else {
                        messageLabel.setText("Art piece not available for purchase.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ArtPiece artPiece = getTableView().getItems().get(getIndex());
                    purchaseButton.setDisable(artPiece.getBuyerEmail() != null);
                    setGraphic(purchaseButton);
                }
            }
        });
        artTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol, actionCol);
        artTable.setItems(javafx.collections.FXCollections.observableArrayList(
                manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() == null)
                        .toList()
        ));
        artTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        content.getChildren().addAll(artTable, messageLabel);
        return content;
    }

    private VBox createPurchasesTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        purchasesTable = new TableView<>(); // Initialize the TableView
        purchasesTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<ArtPiece, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        TableColumn<ArtPiece, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        TableColumn<ArtPiece, String> artistCol = new TableColumn<>("Artist");
        artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist().getName()));
        TableColumn<ArtPiece, String> typeCol = new TableColumn<>("Type");
        TableColumn<ArtPiece, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        purchasesTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol);
        updatePurchasesTableItems(); // Set initial items
        purchasesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        content.getChildren().addAll(purchasesTable, messageLabel);
        return content;
    }

    private void updatePurchasesTableItems() {
        // Update TableView with current purchased art pieces for this buyer
        purchasesTable.setItems(javafx.collections.FXCollections.observableArrayList(
                manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() != null && art.getBuyerEmail().equalsIgnoreCase(buyerEmail))
                        .toList()
        ));
    }
}