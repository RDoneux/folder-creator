package fc.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneManager {

    private HashMap<String, Parent> scenes = new HashMap<>();
    private final Logger log = Logger.getLogger(SceneManager.class.getName());

    private static final Integer INITIAL_WIDTH = 700;
    private static final Integer INITIAL_HEIGHT = 650;

    public SceneManager() {
        // EMPTY
    }

    public void loadScenes(String[] sceneLocations) {
        FXMLLoader loader;
        for (String url : sceneLocations) {
            loader = new FXMLLoader(this.getClass().getResource(url));
            try {
                addScene(stripUrl(url), loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info(sceneLocations.length + " scenes loaded");

    }

    private String stripUrl(String url) {
        return url.replaceAll("[./]", "").replace("fxml", "");
    }

    public void addScene(String key, Parent parent) {
        if (key.isEmpty()) {
            log.warning("scene key cannot be empty - skipping adding scene");
            throw new IllegalArgumentException();
        }
        if (parent == null) {
            log.warning("scene cannot be empty - skipping adding scene");
            throw new IllegalArgumentException();
        }
        scenes.put(key, parent);
        log.info("added new key pair. Key: " + key + " Parent: " + parent);
    }

    public Scene changeScene(Scene scene, String key) {
        if (scene == null) {
            log.info("scene is null... creating new scene");
            scene = new Scene(getScene(key), INITIAL_WIDTH, INITIAL_HEIGHT);
        }
        scene.setRoot(getScene(key));
        return scene;
    }

    private Parent getScene(String key) {
        if (scenes.containsKey(key)) {
            return scenes.get(key);
        }
        throw new IllegalArgumentException("requested scene does not exist. Key: " + key);
    }

}
