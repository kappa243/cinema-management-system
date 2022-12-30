package pl.edu.agh.cinema.ui.userManager.editUserDialog;

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
    protected Button confirmButton;

    @FXML
    private VBox dialogFields;
    @FXML
    protected UserDialogFieldController dialogFieldsController;

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
        dialogFieldsController.firstName.setText(user.getFirstName());
        dialogFieldsController.lastName.setText(user.getLastName());
        dialogFieldsController.email.setText(user.getEmail());
        dialogFieldsController.roleChoiceBox.setValue(user.getRole());
    }

    protected void updateModel(boolean updatePassword) {
        user.setFirstName(dialogFieldsController.firstName.getText());
        user.setLastName(dialogFieldsController.lastName.getText());
        user.setEmail(dialogFieldsController.email.getText());
        if (updatePassword) {
            String hashed = BCrypt.hashpw(dialogFieldsController.password.getText(), BCrypt.gensalt());
            user.setPassword(hashed);
        }
        user.setRole(dialogFieldsController.roleChoiceBox.getValue());
    }

    @FXML
    protected void initialize() {
        dialogFieldsController.roleChoiceBox.getItems().addAll(Role.values());
    }

    protected void handleConfirmAction(ActionEvent event) {
        boolean validatePassword = !dialogFieldsController.password.getText().isEmpty();
        if (dialogFieldsController.validateInput(validatePassword)) {
            updateModel(validatePassword);

            confirmed = true;
            stage.close();
        }
    }

}
