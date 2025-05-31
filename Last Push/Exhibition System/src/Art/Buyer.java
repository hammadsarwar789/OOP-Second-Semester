package Art;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Buyer {
    private static final String DOWNLOAD_DIRECTORY = "D:\\OOP\\Last Push\\SlipImages";
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;
    private final String buyerEmail;
    private TableView<ArtPiece> purchasesTable;
    private Label totalPurchasedLabel;

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

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("Buyer Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setEffect(new DropShadow(5, Color.web("rgba(0,0,0,0.2)")));
        header.getChildren().add(titleLabel);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        Tab galleryTab = new Tab("Art Gallery");
        galleryTab.setClosable(false);
        galleryTab.setContent(createGalleryTab());

        Tab purchasesTab = new Tab("My Purchases");
        purchasesTab.setClosable(false);
        purchasesTab.setContent(createPurchasesTab());

        tabPane.getTabs().addAll(galleryTab, purchasesTab);

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

        Scene scene = new Scene(root, 1550, 750);
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

    private VBox createGalleryTab() {
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
                        showPaymentDialog(artPiece, messageLabel, artTable);
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
        artTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol, imageCol, actionCol);
        artTable.setItems(javafx.collections.FXCollections.observableArrayList(
                manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() == null)
                        .toList()
        ));
        artTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        content.getChildren().addAll(artTable, messageLabel);
        return content;
    }

    private void showPaymentDialog(ArtPiece artPiece, Label messageLabel, TableView<ArtPiece> artTable) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Payment Details");
        dialog.setHeaderText("Enter Payment Information for Art ID: " + artPiece.getId());

        dialog.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        ButtonType payButtonType = new ButtonType("Pay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> bankComboBox = new ComboBox<>();
        bankComboBox.getItems().addAll("HBL", "Bank Al-Habib", "Meeza Bank", "Allied Bank", "Habib Metro");
        bankComboBox.setPromptText("Select Bank");
        bankComboBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        TextField accountNumberField = new TextField();
        accountNumberField.setPromptText("Enter 13-digit Account Number");
        accountNumberField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 12;");

        grid.add(new Label("Bank:"), 0, 0);
        grid.add(bankComboBox, 1, 0);
        grid.add(new Label("Account Number:"), 0, 1);
        grid.add(accountNumberField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(errorLabel, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);

        Button payButton = (Button) dialog.getDialogPane().lookupButton(payButtonType);
        payButton.setDisable(true);

        accountNumberField.textProperty().addListener((obs, old, newValue) -> {
            boolean valid = bankComboBox.getValue() != null &&
                    newValue.matches("\\d{13}") &&
                    !passwordField.getText().isEmpty();
            payButton.setDisable(!valid);
        });
        passwordField.textProperty().addListener((obs, old, newValue) -> {
            boolean valid = bankComboBox.getValue() != null &&
                    accountNumberField.getText().matches("\\d{13}") &&
                    !newValue.isEmpty();
            payButton.setDisable(!valid);
        });
        bankComboBox.valueProperty().addListener((obs, old, newValue) -> {
            boolean valid = newValue != null &&
                    accountNumberField.getText().matches("\\d{13}") &&
                    !passwordField.getText().isEmpty();
            payButton.setDisable(!valid);
        });

        styleButton(payButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == payButtonType) {
                String accountNumber = accountNumberField.getText();
                if (!accountNumber.matches("\\d{13}")) {
                    errorLabel.setText("Account number must be 13 digits.");
                    return null;
                }
                System.out.println("Processing payment for Art ID: " + artPiece.getId() +
                        ", Bank: " + bankComboBox.getValue() +
                        ", Account: " + accountNumber);

                // Set bankName and accountNumber in the ArtPiece object
                artPiece.setBankName(bankComboBox.getValue());
                artPiece.setAccountNumber(accountNumber);

                // Generate QR code before completing the purchase
                try {
                    generateQRCode(artPiece);
                } catch (Exception e) {
                    System.err.println("Failed to generate QR code: " + e.getMessage());
                }

                artPiece.setBuyerEmail(buyerEmail);
                manager.saveToFile();
                messageLabel.setText("Art piece purchased successfully!");
                updatePurchasesTableItems();
                updateTotalPurchased();
                artTable.setItems(javafx.collections.FXCollections.observableArrayList(
                        manager.getAllArtPieces().stream()
                                .filter(art -> art.getBuyerEmail() == null)
                                .toList()
                ));

                String slipContent = generateSalesSlip(artPiece, bankComboBox.getValue(), accountNumber);
                showPurchaseConfirmationDialog(artPiece, slipContent);
                return ButtonType.OK;
            }
            return null;
        });

        dialog.getDialogPane().setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), dialog.getDialogPane());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        dialog.setOnShown(event -> fadeIn.play());

        dialog.showAndWait();
    }

    private void generateQRCode(ArtPiece artPiece) throws WriterException, IOException {
        String artId = artPiece.getId();
        String qrContent = "Art ID: " + artId + "\nTitle: " + artPiece.getTitle() + "\nPrice: $" + artPiece.getPrice();

        File dir = new File(DOWNLOAD_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File qrCodeFile = new File(DOWNLOAD_DIRECTORY, "ArtQRCode_" + artId + ".png");
        if (!qrCodeFile.exists()) {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 100, 100);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrCodeFile.toPath());
            System.out.println("Generated QR code for Art ID: " + artId + " at: " + qrCodeFile.getAbsolutePath());
        } else {
            System.out.println("QR code already exists for Art ID: " + artId);
        }
    }

    private VBox createPurchasesTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        purchasesTable = new TableView<>();
        purchasesTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
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
        purchasesTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol, imageCol);
        updatePurchasesTableItems();
        purchasesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        totalPurchasedLabel = new Label();
        totalPurchasedLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        totalPurchasedLabel.setStyle("-fx-text-fill: #1a3c6c;");
        updateTotalPurchased();

        content.getChildren().addAll(purchasesTable, totalPurchasedLabel, messageLabel);
        return content;
    }

    private void updatePurchasesTableItems() {
        purchasesTable.setItems(javafx.collections.FXCollections.observableArrayList(
                manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() != null && art.getBuyerEmail().equalsIgnoreCase(buyerEmail))
                        .toList()
        ));
    }

    private void updateTotalPurchased() {
        double totalPurchased = manager.getAllArtPieces().stream()
                .filter(art -> art.getBuyerEmail() != null && art.getBuyerEmail().equalsIgnoreCase(buyerEmail))
                .mapToDouble(ArtPiece::getPrice)
                .sum();
        totalPurchasedLabel.setText("Total Purchase Value: $" + String.format("%.2f", totalPurchased));
        System.out.println("Updated total purchase value: $" + totalPurchased);
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
        if (!imageFile.isAbsolute()) {
            imageFile = new File(System.getProperty("user.dir"), artPiece.getImagePath());
        }

        if (!imageFile.exists()) {
            System.out.println("Image file not found: " + imageFile.getAbsolutePath());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Image Not Found");
            alert.setHeaderText(null);
            alert.setContentText("The image file could not be found at: " + imageFile.getAbsolutePath());
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

    private String generateSalesSlip(ArtPiece artPiece, String bankName, String accountNumber) {
        System.out.println("Generating sales slip for Art ID: " + (artPiece != null ? artPiece.getId() : "null"));
        if (artPiece == null) {
            String errorSlip = "=== Sales Slip ===\nError: Art piece is null\n==================";
            System.out.println("Sales slip error: " + errorSlip);
            return errorSlip;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a zzz, MMMM dd, yyyy");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        String purchaseDate = now.format(formatter);
        String artistName = "Unknown Artist";
        try {
            artistName = (artPiece.getArtist() != null && artPiece.getArtist().getName() != null) ? artPiece.getArtist().getName() : "Unknown Artist";
        } catch (Exception e) {
            System.err.println("Error accessing artist name: " + e.getMessage());
        }

        String maskedAccount = "****" + accountNumber.substring(accountNumber.length() - 4);
        String slipContent = "=== Sales Slip ===\n" +
                "Purchase Date: " + purchaseDate + "\n" +
                "Buyer Email: " + (buyerEmail != null ? buyerEmail : "Unknown") + "\n" +
                "Art ID: " + (artPiece.getId() != null ? artPiece.getId() : "N/A") + "\n" +
                "Title: " + (artPiece.getTitle() != null ? artPiece.getTitle() : "N/A") + "\n" +
                "Artist: " + artistName + "\n" +
                "Type: " + (artPiece.getType() != null ? artPiece.getType() : "N/A") + "\n" +
                "Price: $" + String.format("%.2f", artPiece.getPrice()) + "\n" +
                "Bank: " + (bankName != null ? bankName : "N/A") + "\n" +
                "Account Number: " + maskedAccount + "\n" +
                "==================";
        System.out.println("Generated sales slip:\n" + slipContent);
        return slipContent;
    }

    private File generateSlipImage(ArtPiece artPiece, String slipContent) {
        System.out.println("Generating slip image for Art ID: " + (artPiece != null ? artPiece.getId() : "null"));
        try {
            Label slipLabel = new Label(slipContent.isEmpty() ? "No sales slip content" : slipContent);
            slipLabel.setFont(Font.font("System", 12));
            slipLabel.setTextFill(Color.BLACK);
            slipLabel.setWrapText(true);
            slipLabel.setMaxWidth(300);
            slipLabel.setPadding(new Insets(10));
            slipLabel.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");

            VBox tempPane = new VBox(10, slipLabel);
            tempPane.setAlignment(Pos.CENTER);
            tempPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            String artId = artPiece != null ? artPiece.getId() : "unknown";
            File qrCodeFile = new File(DOWNLOAD_DIRECTORY, "ArtQRCode_" + artId + ".png");
            if (qrCodeFile.exists()) {
                Image qrImage = new Image(qrCodeFile.toURI().toString());
                ImageView qrView = new ImageView(qrImage);
                qrView.setFitWidth(100);
                qrView.setFitHeight(100);
                qrView.setPreserveRatio(true);
                tempPane.getChildren().add(qrView);
                System.out.println("Added QR code to slip image: " + qrCodeFile.getAbsolutePath());
            } else {
                System.err.println("QR code file not found: " + qrCodeFile.getAbsolutePath());
                Label qrErrorLabel = new Label("QR Code Not Available");
                qrErrorLabel.setFont(Font.font("System", 10));
                qrErrorLabel.setTextFill(Color.RED);
                tempPane.getChildren().add(qrErrorLabel);
            }

            Scene tempScene = new Scene(tempPane);
            tempPane.layout();
            WritableImage snapshot = tempPane.snapshot(null, null);
            File imageFile = new File(DOWNLOAD_DIRECTORY, "SalesSlip_" + artId + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", imageFile);
            System.out.println("Generated slip image at: " + imageFile.getAbsolutePath());
            return imageFile;
        } catch (Exception e) {
            System.err.println("Error generating slip image: " + e.getMessage());
            return null;
        }
    }

    private void showPurchaseConfirmationDialog(ArtPiece artPiece, String slipContent) {
        System.out.println("Starting showPurchaseConfirmationDialog for Art ID: " + (artPiece != null ? artPiece.getId() : "null"));

        File slipImageFile = generateSlipImage(artPiece, slipContent);
        if (slipImageFile == null || !slipImageFile.exists()) {
            System.err.println("Slip image not generated, proceeding with text slip");
        }

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        content.setAlignment(Pos.CENTER);

        Label slipLabel = new Label(slipContent.isEmpty() ? "No sales slip content" : slipContent);
        slipLabel.setFont(Font.font("System", 12));
        slipLabel.setWrapText(true);
        slipLabel.setMaxWidth(400);
        content.getChildren().add(slipLabel);

        String artId = artPiece != null ? artPiece.getId() : "unknown";
        File qrCodeFile = new File(DOWNLOAD_DIRECTORY, "ArtQRCode_" + artId + ".png");
        if (qrCodeFile.exists()) {
            Image qrImage = new Image(qrCodeFile.toURI().toString());
            ImageView qrView = new ImageView(qrImage);
            qrView.setFitWidth(100);
            qrView.setFitHeight(100);
            qrView.setPreserveRatio(true);
            content.getChildren().add(qrView);
            System.out.println("Displayed QR code in dialog: " + qrCodeFile.getAbsolutePath());
        } else {
            System.err.println("QR code file not found for dialog: " + qrCodeFile.getAbsolutePath());
            Label qrErrorLabel = new Label("QR Code Not Available");
            qrErrorLabel.setFont(Font.font("System", 10));
            qrErrorLabel.setTextFill(Color.RED);
            content.getChildren().add(qrErrorLabel);
        }

        Button downloadTextButton = new Button("Download Slip (Text)");
        styleButton(downloadTextButton);
        downloadTextButton.setOnAction(e -> {
            File dir = new File(DOWNLOAD_DIRECTORY);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, "sales_slip_" + artId + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(slipContent);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Text slip saved to:\n" + file.getAbsolutePath());
                successAlert.showAndWait();
            } catch (IOException ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error"); // Fixed: 'alert' to 'errorAlert'
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to save slip: " + ex.getMessage());
                errorAlert.showAndWait();
            }
        });

        Button downloadImageButton = new Button("Download Slip (Image)");
        styleButton(downloadImageButton);
        downloadImageButton.setOnAction(e -> {
            if (slipImageFile != null && slipImageFile.exists()) {
                File dir = new File(DOWNLOAD_DIRECTORY);
                if (!dir.exists()) dir.mkdirs();

                File destFile = new File(dir, "SalesSlip_" + artId + ".png");
                try {
                    Files.copy(slipImageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Saved slip image to: " + destFile.getAbsolutePath());
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Image slip saved to:\n" + destFile.getAbsolutePath());
                    successAlert.showAndWait();
                } catch (IOException ex) {
                    System.err.println("Error saving slip image: " + ex.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Error Saving Slip Image");
                    errorAlert.setContentText("Failed to save image slip: " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            } else {
                System.err.println("No slip image available to save");
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("No Image Available");
                errorAlert.setContentText("No sales slip image generated. Check logs for errors.");
                errorAlert.showAndWait();
            }
        });

        content.getChildren().addAll(downloadTextButton, downloadImageButton);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase Confirmation");
        alert.setHeaderText("Sales Slip for Art ID: " + (artPiece != null ? artPiece.getId() : "N/A"));
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        alert.getDialogPane().setMinWidth(500);
        alert.getDialogPane().setMinHeight(400);

        alert.getDialogPane().setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), alert.getDialogPane());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        alert.setOnShown(event -> {
            System.out.println("Dialog shown, content bounds: " + content.getBoundsInParent());
            fadeIn.play();
        });

        alert.showAndWait();
        System.out.println("Displayed purchase confirmation dialog");
    }
}