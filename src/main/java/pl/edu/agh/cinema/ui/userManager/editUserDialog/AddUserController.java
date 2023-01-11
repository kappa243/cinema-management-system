package pl.edu.agh.cinema.ui.userManager.editUserDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.auth.AuthenticationService;

@Component
@Scope("prototype")
public class AddUserController extends CommonUserController {

    private final AuthenticationService authenticationService;

    public AddUserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        confirmButton.setOnAction(this::handleConfirmAction);
    }

    @Override
    protected void handleConfirmAction(ActionEvent event) {
        if (userDialogFieldsController.validateInput(true)) {
            try {
                authenticationService.registerUser(userDialogFieldsController.firstName.getText(),
                        userDialogFieldsController.lastName.getText(),
                        userDialogFieldsController.email.getText(),
                        userDialogFieldsController.password.getText(),
                        userDialogFieldsController.roleChoiceBox.getValue());
            } catch (IllegalArgumentException e) {
                userDialogFieldsController.warningMessage.setText(e.getMessage());
                return;
            }

            confirmed = true;
            stage.close();
        }
    }
}
