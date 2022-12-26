package pl.edu.agh.cinema.ui.userManager.editDialog;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.user.Role;

@Component
@Scope("prototype")
public class DialogFieldController {

    @FXML
    public TextField firstName;

    @FXML
    public TextField lastName;

    @FXML
    public TextField email;

    @FXML
    public PasswordField password;

    @FXML
    public ChoiceBox<Role> roleChoiceBox;

    @FXML
    public Label warningMessage;

    public boolean validateInput(boolean updatePassword) {
        // TODO - implement password validation (contains at least one digit, one uppercase letter, one lowercase letter, one special character, set minimal length)
        // TODO - implement update password only if updatePassword true
        // TODO - implement is role selected validation
        if (firstName.getText().matches(".*\\d.*") || firstName.getText().isEmpty()) {
            warningMessage.setText("First name should contain only letters!");
            return false;
        }
        else if (lastName.getText().matches(".*\\d.*") || lastName.getText().isEmpty()) {
            warningMessage.setText("Last name should contain only letters!");
            return false;
        }
        else if (!email.getText().matches("^[\\w.]+@([\\w-]+.)+[\\w-]{2,4}$") || email.getText().isEmpty()) {
            warningMessage.setText("Wrong email address!");
            return false;
        }
        else warningMessage.setText("");
        return true;
    }


}
