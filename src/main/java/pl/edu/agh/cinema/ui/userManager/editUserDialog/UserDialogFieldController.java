package pl.edu.agh.cinema.ui.userManager.editUserDialog;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.user.Role;
import pl.edu.agh.cinema.ui.StageAware;


@Component
@Scope("prototype")
public class UserDialogFieldController implements StageAware {
    @Setter
    private Stage stage;
    @FXML
    public MFXTextField firstName;

    @FXML
    public MFXTextField lastName;

    @FXML
    public MFXTextField email;

    @FXML
    public MFXPasswordField password;

    @FXML
    public MFXComboBox<Role> roleChoiceBox;

    @FXML
    public Label warningMessage;

    public boolean validateInput(boolean updatePassword) {

        if (!firstName.getText().matches("[A-Z]{1}[a-z]+") || firstName.getText().isEmpty()) {
            warningMessage.setText("First name should contain only letters and start with uppercase!");
            return false;
        }

        if (!lastName.getText().matches("[A-Z]{1}[a-z]+") || lastName.getText().isEmpty()) {
            warningMessage.setText("Last name should contain only letters! and start with uppercase");
            return false;
        }

        if (updatePassword) {
            String passwd = password.getText();
            if (passwd.length() < 8 || !passwd.matches(".*[!@#$%&*(),._+=|<>?{}\\[\\]~-].*")
                    || !passwd.matches(".*[A-Z].*")
                    || !passwd.matches(".*\\d.*")) {
                warningMessage.setText("Password should contain at least 8 characters, including at least 1 uppercase" +
                        " letter, 1 digit and 1 special character!");
                return false;
            }
        }

        String RFC5322_EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        if (!email.getText().matches(RFC5322_EMAIL_PATTERN) || email.getText().isEmpty()) {
            warningMessage.setText("Wrong email address!");
            return false;
        }

        if (roleChoiceBox.getSelectionModel().getSelectedItem() == null) {
            warningMessage.setText("Role not set!");
            return false;
        }

        warningMessage.setText("");
        return true;
    }


}
