package fc.frontend;

import javafx.fxml.FXML;

public class SettingsPageController {

    @FXML
    public void handleSaveButtonPress() {

    }

    @FXML
    public void handleReturnButtonPress() {
        Client.changeScene("main-page");
    }

    @FXML
    public void handleLogoClick() {
        Client.changeScene("main-page");
    }

}
