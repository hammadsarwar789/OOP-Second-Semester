package Art;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginView {
    private final ArtExhibitionManager manager;
    private final UserManager userManager;
    private final Stage stage;
    private Label messageLabel;
    private ProgressIndicator progressIndicator;
    private BorderPane root;
    private TextField emailField;
    private PasswordField passwordField;
    private static final String PRIMARY_COLOR = "#1a2634";
    private static final String SECONDARY_COLOR = "#3b82f6";
    private static final String ACCENT_COLOR = "#ef4444";
    private static final String SUCCESS_COLOR = "#22c55e";
    private static final String TEXT_COLOR = "#f1f5f9";

    public LoginView(ArtExhibitionManager manager, Stage stage) {
        this.manager = manager;
        this.userManager = new UserManager();
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Art Exhibition System - Login");
        stage.setMaxWidth(1550);
        stage.setMaxHeight(800);

        root = new BorderPane();
        root.setStyle("-fx-background-color: transparent;");
        setupBackground(root);

        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getChildren().addAll(createHeader(), createFormBox());

        root.setCenter(centerContainer);
        root.setBottom(createFooter());

        Scene scene = new Scene(root, 1550, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        animateRoot(root);
    }

    private void setupBackground(BorderPane root) {
        try {
            String imagePath = "file:///D:/OOP/Last%20Push/login.jpg";
            Image backgroundImage = new Image(imagePath, true);
            if (!backgroundImage.isError()) {
                BackgroundImage backgroundImg = new BackgroundImage(
                        backgroundImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
                );
                root.setBackground(new Background(backgroundImg));
            }
        } catch (Exception e) {
            Stop[] stops = new Stop[] {
                    new Stop(0, Color.web(PRIMARY_COLOR)),
                    new Stop(1, Color.web(SECONDARY_COLOR))
            };
            LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
            root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30, 20, 30, 20));
        header.setStyle("-fx-background-color: rgba(44, 62, 80, 0.95); -fx-background-radius: 20;"); // New header color

        Label titleLabel = new Label("Art Exhibition Gallery");
        titleLabel.setFont(Font.font("System", FontWeight.BLACK, 40));
        titleLabel.setTextFill(Color.web(TEXT_COLOR));
        titleLabel.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));

        Label subtitleLabel = new Label("Discover the World of Artistic Expression");
        subtitleLabel.setFont(Font.font("System", FontWeight.MEDIUM, 16));
        subtitleLabel.setTextFill(Color.web(TEXT_COLOR).deriveColor(0, 1, 1, 0.8));

        header.getChildren().addAll(titleLabel, subtitleLabel);
        animateHeader(header);
        return header;
    }

    private VBox createFormBox() {
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40));
        formBox.setMaxWidth(450);
        formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.98); -fx-background-radius: 25; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);");

        emailField = createStyledTextField("Enter your email", "email-icon.png");
        passwordField = createStyledPasswordField("Enter your password", "password-icon.png");

        Button loginButton = createStyledButton("Sign In", SECONDARY_COLOR);
        HBox registerBox = new HBox(15);
        registerBox.setAlignment(Pos.CENTER);
        Button registerSellerButton = createStyledButton("Join as Seller", SUCCESS_COLOR);
        Button registerBuyerButton = createStyledButton("Join as Buyer", SUCCESS_COLOR);
        registerBox.getChildren().addAll(registerSellerButton, registerBuyerButton);

        Button forgotPasswordButton = createStyledTextButton("Forgot Password?");
        forgotPasswordButton.setTooltip(new Tooltip("Recover your account password"));

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14; -fx-font-weight: bold;");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(40, 40);
        progressIndicator.setStyle("-fx-progress-color: " + SECONDARY_COLOR + ";");

        setupButtonActions(emailField, passwordField, loginButton, registerSellerButton,
                registerBuyerButton, forgotPasswordButton);

        formBox.getChildren().addAll(
                emailField, passwordField, loginButton,
                registerBox, forgotPasswordButton,
                messageLabel, progressIndicator
        );

        animateFormBox(formBox);
        return formBox;
    }

    private TextField createStyledTextField(String prompt, String iconName) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e5e7eb; " +
                "-fx-border-radius: 12; -fx-padding: 12 15; -fx-font-size: 15; " +
                "-fx-background-radius: 12; -fx-text-fill: #1f2937;");
        field.setPrefHeight(50);
        field.setMaxWidth(400);
        field.setTooltip(new Tooltip("Enter a valid email address"));

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #ffffff; -fx-border-color: " + SECONDARY_COLOR + "; " +
                        "-fx-border-width: 2; -fx-border-radius: 12; -fx-padding: 12 15; " +
                        "-fx-font-size: 15; -fx-background-radius: 12; -fx-text-fill: #1f2937;");
                animateField(field, 1.03);
            } else {
                field.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e5e7eb; " +
                        "-fx-border-radius: 12; -fx-padding: 12 15; -fx-font-size: 15; " +
                        "-fx-background-radius: 12; -fx-text-fill: #1f2937;");
                animateField(field, 1.0);
            }
        });

        return field;
    }

    private PasswordField createStyledPasswordField(String prompt, String iconName) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e5e7eb; " +
                "-fx-border-radius: 12; -fx-padding: 12 15; -fx-font-size: 15; " +
                "-fx-background-radius: 12; -fx-text-fill: #1f2937;");
        field.setPrefHeight(50);
        field.setMaxWidth(400);
        field.setTooltip(new Tooltip("Enter your password (minimum 8 characters)"));

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #ffffff; -fx-border-color: " + SECONDARY_COLOR + "; " +
                        "-fx-border-width: 2; -fx-border-radius: 12; -fx-padding: 12 15; " +
                        "-fx-font-size: 15; -fx-background-radius: 12; -fx-text-fill: #1f2937;");
                animateField(field, 1.03);
            } else {
                field.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e5e7eb; " +
                        "-fx-border-radius: 12; -fx-padding: 12 15; -fx-font-size: 15; " +
                        "-fx-background-radius: 12; -fx-text-fill: #1f2937;");
                animateField(field, 1.0);
            }
        });

        return field;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        String darkerColor = darkenColor(color);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, " + color + ", " + darkerColor + "); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; " +
                "-fx-background-radius: 12; -fx-font-size: 15; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        button.setPrefHeight(50);
        button.setMaxWidth(200);

        button.setOnMouseEntered(e -> {
            String lighterColor = lightenColor(color);
            button.setStyle("-fx-background-color: linear-gradient(to bottom, " + lighterColor + ", " + color + "); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; " +
                    "-fx-background-radius: 12; -fx-font-size: 15; -fx-cursor: hand; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 3);");
            animateButton(button, 1.08);
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: linear-gradient(to bottom, " + color + ", " + darkerColor + "); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; " +
                    "-fx-background-radius: 12; -fx-font-size: 15; -fx-cursor: hand; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
            animateButton(button, 1.0);
        });

        return button;
    }

    private Button createStyledTextButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + SECONDARY_COLOR + "; " +
                "-fx-font-size: 14; -fx-cursor: hand; -fx-underline: true; " +
                "-fx-font-weight: 500;");

        button.setOnMouseEntered(e -> {
            String lighterColor = lightenColor(SECONDARY_COLOR);
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + lighterColor + "; " +
                    "-fx-font-size: 14; -fx-cursor: hand; -fx-underline: true; " +
                    "-fx-font-weight: 500;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + SECONDARY_COLOR + "; " +
                    "-fx-font-size: 14; -fx-cursor: hand; -fx-underline: true; " +
                    "-fx-font-weight: 500;");
        });

        return button;
    }

    private void setupButtonActions(TextField emailField, PasswordField passwordField,
                                    Button loginButton, Button registerSellerButton,
                                    Button registerBuyerButton, Button forgotPasswordButton) {
        loginButton.setOnAction(e -> {
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText().trim();

            if (validateInput(email, password)) {
                showLoading(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        javafx.application.Platform.runLater(() -> {
                            handleLogin(email, password);
                            showLoading(false);
                        });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                animateFieldError(this.emailField, this.passwordField);
            }
        });

        registerSellerButton.setOnAction(e -> {
            TextInputDialog emailDialog = new TextInputDialog();
            styleDialog(emailDialog, "Register as Seller", "Create your seller account", "Email:");
            emailDialog.showAndWait().ifPresent(email -> {
                if (userManager.getUsers().containsKey(email.toLowerCase())) {
                    showError("Email already registered");
                    return;
                }
                if (!isValidEmail(email)) {
                    showError("Invalid email format");
                    return;
                }
                TextInputDialog passwordDialog = new TextInputDialog();
                styleDialog(passwordDialog, "Register as Seller", "Set your password", "Password:");
                passwordDialog.showAndWait().ifPresent(password -> {
                    if (password.length() < 8) {
                        showError("Password must be at least 8 characters");
                        return;
                    }
                    userManager.addUser(email, password, "seller");
                    showSuccess("Seller account created successfully!");
                });
            });
        });

        registerBuyerButton.setOnAction(e -> {
            TextInputDialog emailDialog = new TextInputDialog();
            styleDialog(emailDialog, "Register as Buyer", "Create your buyer account", "Email:");
            emailDialog.showAndWait().ifPresent(email -> {
                if (userManager.getUsers().containsKey(email.toLowerCase())) {
                    showError("Email already registered");
                    return;
                }
                if (!isValidEmail(email)) {
                    showError("Invalid email format");
                    return;
                }
                TextInputDialog passwordDialog = new TextInputDialog();
                styleDialog(passwordDialog, "Register as Buyer", "Set your password", "Password:");
                passwordDialog.showAndWait().ifPresent(password -> {
                    if (password.length() < 8) {
                        showError("Password must be at least 8 characters");
                        return;
                    }
                    userManager.addUser(email, password, "buyer");
                    showSuccess("Buyer account created successfully!");
                });
            });
        });

        forgotPasswordButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            styleDialog(dialog, "Password Recovery", "Enter your registered email", "Email:");
            dialog.showAndWait().ifPresent(email -> {
                User user = userManager.getUsers().get(email.toLowerCase());
                if (user != null) {
                    showSuccess("Your password is: " + user.getPassword());
                } else {
                    showError("Email not found in our system");
                }
            });
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return false;
        }
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return false;
        }
        if (password.length() < 8) {
            showError("Password must be at least 8 characters");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14; -fx-font-weight: bold;");
        animateError();
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + SUCCESS_COLOR + "; -fx-font-size: 14; -fx-font-weight: bold;");
    }

    private void showLoading(boolean show) {
        progressIndicator.setVisible(show);
        if (show) {
            RotateTransition rt = new RotateTransition(Duration.millis(1000), progressIndicator);
            rt.setByAngle(360);
            rt.setCycleCount(RotateTransition.INDEFINITE);
            rt.play();
        }
    }

    private void animateField(TextField field, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), field);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private void animateFieldError(TextField... fields) {
        for (TextField field : fields) {
            RotateTransition rt = new RotateTransition(Duration.millis(50), field);
            rt.setByAngle(5);
            rt.setCycleCount(4);
            rt.setAutoReverse(true);
            rt.play();
        }
    }

    private void animateButton(Button button, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private void animateRoot(BorderPane root) {
        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(800), root);
        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1.0);
        scale.setToY(1.0);

        SequentialTransition sequence = new SequentialTransition(fadeIn, scale);
        sequence.play();
    }

    private void animateHeader(VBox header) {
        header.setOpacity(0);
        header.setTranslateY(-20);
        FadeTransition fade = new FadeTransition(Duration.millis(1000), header);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate = new TranslateTransition(Duration.millis(1000), header);
        translate.setToY(0);

        SequentialTransition sequence = new SequentialTransition(fade, translate);
        sequence.play();
    }

    private void animateFormBox(VBox formBox) {
        formBox.setOpacity(0);
        formBox.setScaleX(0.9);
        formBox.setScaleY(0.9);

        FadeTransition fade = new FadeTransition(Duration.millis(1200), formBox);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(1200), formBox);
        scale.setToX(1.0);
        scale.setToY(1.0);

        SequentialTransition sequence = new SequentialTransition(fade, scale);
        sequence.setDelay(Duration.millis(300));
        sequence.play();
    }

    private void animateError() {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), messageLabel);
        st.setFromX(1.0);
        st.setToX(1.15);
        st.setFromY(1.0);
        st.setToY(1.15);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15));
        footer.setStyle("-fx-background-color: rgba(52, 73, 94, 0.95); -fx-background-radius: 20 20 0 0;"); // New footer color

        Label footerLabel = new Label("© 2025 Art Exhibition System. All Rights Reserved.");
        footerLabel.setTextFill(Color.web(TEXT_COLOR));
        footerLabel.setFont(Font.font("System", FontWeight.MEDIUM, 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Hyperlink termsLink = new Hyperlink("Terms");
        termsLink.setStyle("-fx-text-fill: " + SECONDARY_COLOR + "; -fx-font-size: 12; -fx-border-width: 0;");
        termsLink.setOnAction(e -> showTermsDialog());

        Hyperlink privacyLink = new Hyperlink("Privacy");
        privacyLink.setStyle("-fx-text-fill: " + SECONDARY_COLOR + "; -fx-font-size: 12; -fx-border-width: 0;");
        privacyLink.setOnAction(e -> showPrivacyDialog());

        footer.getChildren().addAll(footerLabel, spacer, termsLink, privacyLink);
        return footer;
    }

    private String darkenColor(String color) {
        Color c = Color.web(color);
        return c.darker().toString().replace("0x", "#");
    }

    private String lightenColor(String color) {
        Color c = Color.web(color);
        return c.brighter().toString().replace("0x", "#");
    }

    private void styleDialog(Dialog<?> dialog, String title, String header, String content) {
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        dialog.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; " +
                "-fx-border-color: #e5e7eb; -fx-border-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);");
        dialog.getDialogPane().getButtonTypes().clear();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    private void showTermsDialog() {
        Dialog<Void> dialog = new Dialog<>();
        styleDialog(dialog, "Terms of Service", "Terms of Service", "Please review our terms of service.");
        dialog.showAndWait();
    }

    private void showPrivacyDialog() {
        Dialog<Void> dialog = new Dialog<>();
        styleDialog(dialog, "Privacy Policy", "Privacy Policy", "Please review our privacy policy.");
        dialog.showAndWait();
    }

    private void handleLogin(String email, String password) {
        User user = userManager.getUsers().get(email);
        if (user != null && user.getPassword().equals(password)) {
            showSuccess("Login successful!");
            FadeTransition fade = new FadeTransition(Duration.millis(600), root);
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
                        showError("Unknown role.");
                }
            });
            fade.play();
        } else {
            showError("Invalid email or password.");
            animateFieldError(emailField, passwordField);
        }
    }
}
