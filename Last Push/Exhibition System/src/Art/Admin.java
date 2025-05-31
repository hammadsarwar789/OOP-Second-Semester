package Art;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import javafx.util.Duration;
import java.io.File;
import java.util.Optional;

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

        tabPane.getTabs().addAll(usersTab, artTab);

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

    private VBox createUsersTab() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: #ffffff;");

        Label usersLabel = new Label("Manage Users");
        usersLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        usersLabel.setTextFill(Color.web("#1a3c6c"));

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        TableView<User> usersTable = new TableView<>();
        usersTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));
        TableColumn<User, Double> totalTransCol = new TableColumn<>("Total Transactions ($)");
        totalTransCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<User, Double> data) {
                User user = data.getValue();
                String email = user.getEmail().toLowerCase();
                String role = user.getRole().toLowerCase();
                double total = 0.0;

                if (role.equals("buyer")) {
                    total = manager.getAllArtPieces().stream()
                            .filter(art -> art.getBuyerEmail() != null && art.getBuyerEmail().equalsIgnoreCase(email))
                            .mapToDouble(ArtPiece::getPrice)
                            .sum();
                } else if (role.equals("seller")) {
                    total = manager.getAllArtPieces().stream()
                            .filter(art -> art.getBuyerEmail() != null && art.getSellerEmail() != null && art.getSellerEmail().equalsIgnoreCase(email))
                            .mapToDouble(ArtPiece::getPrice)
                            .sum();
                }
                return new SimpleDoubleProperty(total).asObject();
            }
        });
        totalTransCol.setCellFactory(column -> new TableCell<User, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        usersTable.getColumns().addAll(emailCol, roleCol, totalTransCol);
        usersTable.setItems(javafx.collections.FXCollections.observableArrayList(userManager.getUsers().values()));
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox actionBox = new HBox(10);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter user email");
        emailField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 8;");

        Button addButton = new Button("Add User");
        Button removeButton = new Button("Remove User");
        Button viewTransactionsButton = new Button("View Transactions");

        styleButton(addButton);
        styleButton(removeButton);
        styleButton(viewTransactionsButton);

        actionBox.getChildren().addAll(emailField, addButton, removeButton, viewTransactionsButton);

        addButton.setOnAction(e -> {
            String email = emailField.getText().trim().toLowerCase();
            if (email.isEmpty()) {
                messageLabel.setText("Please enter an email.");
                return;
            }
            if (userManager.getUsers().containsKey(email)) {
                messageLabel.setText("User with email '" + email + "' already exists.");
                return;
            }

            ChoiceDialog<String> roleDialog = new ChoiceDialog<>("buyer", "buyer", "seller");
            roleDialog.setTitle("Add User");
            roleDialog.setHeaderText("Select User Role");
            roleDialog.setContentText("Role:");
            Optional<String> roleResult = roleDialog.showAndWait();

            if (roleResult.isPresent()) {
                String role = roleResult.get();
                TextInputDialog passwordDialog = new TextInputDialog();
                passwordDialog.setTitle("Add User");
                passwordDialog.setHeaderText("Enter Password for " + email);
                passwordDialog.setContentText("Password:");
                Optional<String> passwordResult = passwordDialog.showAndWait();

                if (passwordResult.isPresent() && !passwordResult.get().trim().isEmpty()) {
                    String password = passwordResult.get().trim();
                    userManager.addUser(email, role, password);
                    usersTable.setItems(javafx.collections.FXCollections.observableArrayList(userManager.getUsers().values()));
                    messageLabel.setText("User '" + email + "' (" + role + ") added successfully.");
                    emailField.clear();
                } else {
                    messageLabel.setText("Password cannot be empty. User addition cancelled.");
                }
            } else {
                messageLabel.setText("User addition cancelled.");
            }
        });

        removeButton.setOnAction(e -> {
            String email = emailField.getText().trim().toLowerCase();
            if (email.isEmpty()) {
                messageLabel.setText("Please enter an email.");
                return;
            }
            if (email.equalsIgnoreCase(ADMIN_EMAIL)) {
                messageLabel.setText("Cannot remove the admin account.");
                return;
            }
            User user = userManager.getUsers().get(email);
            if (user == null) {
                messageLabel.setText("No user found with email '" + email + "'.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Removal");
            confirmAlert.setHeaderText("Remove User: " + email + " (" + user.getRole() + ")");
            confirmAlert.setContentText("Are you sure you want to remove this user?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                userManager.removeUser(email);
                usersTable.setItems(javafx.collections.FXCollections.observableArrayList(userManager.getUsers().values()));
                messageLabel.setText("User '" + email + "' removed successfully.");
                emailField.clear();
            } else {
                messageLabel.setText("User removal cancelled.");
            }
        });

        viewTransactionsButton.setOnAction(e -> {
            String email = emailField.getText().trim().toLowerCase();
            if (email.isEmpty()) {
                messageLabel.setText("Please enter an email.");
                return;
            }
            User user = userManager.getUsers().get(email);
            if (user == null) {
                messageLabel.setText("No user found with email '" + email + "'.");
                return;
            }

            java.util.List<ArtPiece> transactions = new java.util.ArrayList<>();
            String role = user.getRole().toLowerCase();
            double totalPrice = 0.0;

            if (role.equals("buyer")) {
                transactions = manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() != null && art.getBuyerEmail().equalsIgnoreCase(email))
                        .toList();
                totalPrice = transactions.stream().mapToDouble(ArtPiece::getPrice).sum();
            } else if (role.equals("seller")) {
                transactions = manager.getAllArtPieces().stream()
                        .filter(art -> art.getBuyerEmail() != null && art.getSellerEmail() != null && art.getSellerEmail().equalsIgnoreCase(email))
                        .toList();
                totalPrice = transactions.stream().mapToDouble(ArtPiece::getPrice).sum();
            } else {
                messageLabel.setText("Invalid role for viewing transactions.");
                return;
            }

            TableView<ArtPiece> transactionsTable = new TableView<>();
            transactionsTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d9e2ec; -fx-border-radius: 5;");
            TableColumn<ArtPiece, String> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
            TableColumn<ArtPiece, String> titleCol = new TableColumn<>("Title");
            titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
            TableColumn<ArtPiece, String> artistCol = new TableColumn<>("Artist");
            artistCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtist().getName()));
            TableColumn<ArtPiece, String> transCol = new TableColumn<>(role.equals("buyer") ? "Buyer" : "Seller");
            transCol.setCellValueFactory(data -> new SimpleStringProperty(role.equals("buyer") ? (data.getValue().getBuyerEmail() != null ? data.getValue().getBuyerEmail() : "") : (data.getValue().getSellerEmail() != null ? data.getValue().getSellerEmail() : "")));
            TableColumn<ArtPiece, Double> priceCol = new TableColumn<>("Price");
            priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
            transactionsTable.getColumns().addAll(idCol, titleCol, artistCol, transCol, priceCol);
            transactionsTable.setItems(javafx.collections.FXCollections.observableArrayList(transactions));
            transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            VBox alertContent = new VBox(10);
            alertContent.setPadding(new Insets(10));
            Label totalLabel = new Label("Total Price: $" + String.format("%.2f", totalPrice));
            totalLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            totalLabel.setStyle("-fx-text-fill: #1a3c6c;");
            alertContent.getChildren().addAll(totalLabel, transactionsTable);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transactions for " + email);
            alert.setHeaderText(transactions.isEmpty() ? "No Transactions Found" : (role.equals("buyer") ? "Purchases by " + email : "Sales by " + email));
            alert.getDialogPane().setContent(alertContent);
            alert.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
            alert.showAndWait();

            messageLabel.setText("Displayed transactions for '" + email + "'.");
            emailField.clear();
        });

        content.getChildren().addAll(usersLabel, usersTable, actionBox, messageLabel);
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
        TableColumn<ArtPiece, String> buyerCol = new TableColumn<>("Buyer");
        buyerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuyerEmail() != null ? data.getValue().getBuyerEmail() : ""));
        TableColumn<ArtPiece, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSellerEmail() != null ? data.getValue().getSellerEmail() : ""));
        TableColumn<ArtPiece, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        TableColumn<ArtPiece, String> bankNameCol = new TableColumn<>("Bank Name");
        bankNameCol.setCellValueFactory(data -> {
            ArtPiece art = data.getValue();
            if (art.getBuyerEmail() == null) {
                return new SimpleStringProperty("");
            }
            return new SimpleStringProperty(art.getBankName() != null ? art.getBankName() : "Not Provided");
        });
        TableColumn<ArtPiece, String> accountNumberCol = new TableColumn<>("Account Number");
        accountNumberCol.setCellValueFactory(data -> {
            ArtPiece art = data.getValue();
            if (art.getBuyerEmail() == null) {
                return new SimpleStringProperty("");
            }
            return new SimpleStringProperty(art.getAccountNumber() != null ? "****" + art.getAccountNumber().substring(art.getAccountNumber().length() - 4) : "Not Provided");
        });
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
        artTable.getColumns().addAll(idCol, titleCol, artistCol, typeCol, priceCol, buyerCol, sellerCol, bankNameCol, accountNumberCol, imageCol);
        artTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllArtPieces()));
        artTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        double totalSold = manager.getAllArtPieces().stream()
                .filter(art -> art.getBuyerEmail() != null)
                .mapToDouble(ArtPiece::getPrice)
                .sum();
        Label totalSoldLabel = new Label("Total Sold Value: $" + String.format("%.2f", totalSold));
        totalSoldLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        totalSoldLabel.setStyle("-fx-text-fill: #1a3c6c;");

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

        content.getChildren().addAll(artTable, totalSoldLabel, removeBox, searchBox, messageLabel);
        return content;
    }

    private void removeArtPiece(String id, Label messageLabel, TableView<ArtPiece> artTable) {
        if (id.isEmpty()) {
            messageLabel.setText("Please enter an Art ID.");
            return;
        }
        ArtPiece artPiece = manager.getArtPieceById(id);
        if (artPiece != null) {
            if (artPiece.getBuyerEmail() != null) {
                messageLabel.setText("Cannot remove art piece with ID '" + id + "' as it is already sold.");
                return;
            }
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