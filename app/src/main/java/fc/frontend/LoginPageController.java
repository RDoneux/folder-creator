package fc.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginbutton;

    @FXML
    private void handleLogin() {
        try {

            User user = new UserParser().search(username.getText());

            if (user != null) {
                if (user.getPassword().equals(password.getText())) {
                    System.out.println("login successfull");
                    return;
                }
            }
            System.out.println("login unsuccessfull");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
