package fc.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label error;

    @FXML
    private void handleLogin() {
        try {

            User user = new UserParser().search(username.getText());

            if (user != null) {
                if (user.getPassword().equals(password.getText())) {
                    Client.changeScene("main-page");
                    return;
                }
            }
            error.setOpacity(1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
