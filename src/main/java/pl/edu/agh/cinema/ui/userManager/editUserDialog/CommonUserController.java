package pl.edu.agh.cinema.ui.userManager.editUserDialog;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.edu.agh.cinema.model.user.Role;
import pl.edu.agh.cinema.model.user.User;
import pl.edu.agh.cinema.ui.StageAware;

abstract class CommonUserController implements StageAware {

    @FXML
    protected MFXButton confirmButton;

    @FXML
    private VBox userDialogFields;
    @FXML
    protected UserDialogFieldController userDialogFieldsController;

    @Setter
    protected Stage stage;

    private User user;

    protected boolean confirmed = false;

    public void setData(User user) {
        this.user = user;

        updateContent();
    }

    /**
     * @return Returns true if the user clicked OK, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    protected void updateContent() {
        userDialogFieldsController.firstName.setText(user.getFirstName());
        userDialogFieldsController.lastName.setText(user.getLastName());
        userDialogFieldsController.email.setText(user.getEmail());
        userDialogFieldsController.roleChoiceBox.setValue(user.getRole());
        userDialogFieldsController.roleChoiceBox.selectItem(user.getRole());
        userDialogFieldsController.roleChoiceBox.setText(user.getRole().toString());
    }

    protected void updateModel(boolean updatePassword) {
        user.setFirstName(userDialogFieldsController.firstName.getText());
        user.setLastName(userDialogFieldsController.lastName.getText());
        user.setEmail(userDialogFieldsController.email.getText());
        if (updatePassword) {
            String hashed = BCrypt.hashpw(userDialogFieldsController.password.getText(), BCrypt.gensalt());
            user.setPassword(hashed);
        }
        user.setRole(userDialogFieldsController.roleChoiceBox.getValue());
    }

    @FXML
    protected void initialize() {
        userDialogFieldsController.roleChoiceBox.getItems().addAll(Role.values());
    }

    protected void handleConfirmAction(ActionEvent event) {
        boolean validatePassword = !userDialogFieldsController.password.getText().isEmpty();
        if (userDialogFieldsController.validateInput(validatePassword)) {
            updateModel(validatePassword);

            confirmed = true;
            stage.close();
        }
    }

}
