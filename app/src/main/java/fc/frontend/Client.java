package fc.frontend;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    private static SceneManager sceneManager;
    private static Scene scene;

    private void loadScenes() {
        sceneManager = new SceneManager();
        sceneManager.loadScenes(new String[] { "/login-page.fxml", "/main-page.fxml" });
    }

    public static void changeScene(String key) {
        scene = sceneManager.changeScene(scene, key);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        loadScenes();

        // set the first scene
        changeScene("main-page");

        stage.setTitle("Course Folder Creator");
        stage.setScene(scene);
        stage.show();
    }

}