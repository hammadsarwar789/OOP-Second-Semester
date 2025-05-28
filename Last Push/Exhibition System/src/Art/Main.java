package Art;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static ArtExhibitionManager manager = new ArtExhibitionManager();

    @Override
    public void start(Stage primaryStage) {
        manager.loadFromFile();
        LoginView loginView = new LoginView(manager, primaryStage);
        loginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}