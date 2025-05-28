package Art;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginView {
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;

    public LoginView(ArtExhibitionManager manager, Stage stage) {
        this.manager = manager;
        this.userManager = new UserManager();
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Art Exhibition System - Login");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e6f0fa, #ffffff);");

        // Header
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 0 0 1 0;");

        Label titleLabel = new Label("Art Exhibition Gallery");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#ffffff"));
        titleLabel.setEffect(new DropShadow(5, Color.web("rgba(0,0,0,0.3)")));
        header.getChildren().add(titleLabel);

        // Center: Login Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(400);
        formBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #d9e2ec; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");
        emailField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 10; -fx-font-size: 14;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #d9e2ec; -fx-border-radius: 5; -fx-padding: 10; -fx-font-size: 14;");

        Button loginButton = new Button("Login");
        styleButton(loginButton, "#007bff");

        Button registerSellerButton = new Button("Register as Seller");
        styleButton(registerSellerButton, "#28a745");

        Button registerBuyerButton = new Button("Register as Buyer");
        styleButton(registerBuyerButton, "#28a745");

        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #dc3545; -fx-underline: true; -fx-font-size: 14;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 14;");

        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText().trim();
            User user = userManager.getUsers().get(email);
            if (user != null && user.getPassword().equals(password)) {
                messageLabel.setText("Login successful!");
                FadeTransition fade = new FadeTransition(Duration.millis(500), root);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setOnFinished(event -> {
                    switch (user.getRole().toLowerCase()) {
                        case "admin":
                            new Admin(manager, userManager, stage).show();
                            break;
                        case "seller":
                            new Seller(manager, userManager, stage, user.getEmail()).show();
                            break;
                        case "buyer":
                            new Buyer(manager, userManager, stage, user.getEmail()).show();
                            break;
                        default:
                            messageLabel.setText("Unknown role.");
                    }
                });
                fade.play();
            } else {
                messageLabel.setText("Invalid email or password.");
            }
        });

        registerSellerButton.setOnAction(e -> {
            TextInputDialog emailDialog = new TextInputDialog();
            styleDialog(emailDialog, "Register Seller", "Enter your email:", "Email:");
            emailDialog.showAndWait().ifPresent(email -> {
                if (userManager.getUsers().containsKey(email.toLowerCase())) {
                    messageLabel.setText("Email already registered.");
                    return;
                }
                if (!email.contains("@") || !email.contains(".")) {
                    messageLabel.setText("Invalid email format.");
                    return;
                }
                TextInputDialog passwordDialog = new TextInputDialog();
                styleDialog(passwordDialog, "Register Seller", "Enter your password:", "Password:");
                passwordDialog.showAndWait().ifPresent(password -> {
                    if (password.isEmpty()) {
                        messageLabel.setText("Password cannot be empty.");
                        return;
                    }
                    userManager.addUser(email, password, "seller");
                    messageLabel.setText("Seller registered successfully!");
                });
            });
        });

        registerBuyerButton.setOnAction(e -> {
            TextInputDialog emailDialog = new TextInputDialog();
            styleDialog(emailDialog, "Register Buyer", "Enter your email:", "Email:");
            emailDialog.showAndWait().ifPresent(email -> {
                if (userManager.getUsers().containsKey(email.toLowerCase())) {
                    messageLabel.setText("Email already registered.");
                    return;
                }
                if (!email.contains("@") || !email.contains(".")) {
                    messageLabel.setText("Invalid email format.");
                    return;
                }
                TextInputDialog passwordDialog = new TextInputDialog();
                styleDialog(passwordDialog, "Register Buyer", "Enter your password:", "Password:");
                passwordDialog.showAndWait().ifPresent(password -> {
                    if (password.isEmpty()) {
                        messageLabel.setText("Password cannot be empty.");
                        return;
                    }
                    userManager.addUser(email, password, "buyer");
                    messageLabel.setText("Buyer registered successfully!");
                });
            });
        });

        forgotPasswordButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            styleDialog(dialog, "Forgot Password", "Enter your registered email:", "Email:");
            dialog.showAndWait().ifPresent(email -> {
                User user = userManager.getUsers().get(email.toLowerCase());
                if (user != null) {
                    messageLabel.setText("Your password is: " + user.getPassword());
                } else {
                    messageLabel.setText("Email not found.");
                }
            });
        });

        formBox.getChildren().addAll(
                emailField, passwordField, loginButton,
                registerSellerButton, registerBuyerButton, forgotPasswordButton, messageLabel
        );

        root.setTop(header);
        root.setCenter(formBox);

        // Footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #1a3c6c; -fx-border-color: #d9e2ec; -fx-border-width: 1 0 0 0;");
        Label footerLabel = new Label("© 2025 Art Exhibition System");
        footerLabel.setTextFill(Color.WHITE);
        footerLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        footer.getChildren().add(footerLabel);

        root.setBottom(footer);

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
        stage.show();

        // Fade-in animation
        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void styleButton(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 14;");
        button.setEffect(new DropShadow(3, Color.web("rgba(0,0,0,0.2)")));
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + darkenColor(color) + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 14;");
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 5; -fx-font-size: 14;");
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }

    private void styleDialog(Dialog<?> dialog, String title, String header, String content) {
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #d9e2ec; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
    }

    private String darkenColor(String color) {
        return color.equals("#007bff") ? "#0056b3" : "#218838";
    }
}