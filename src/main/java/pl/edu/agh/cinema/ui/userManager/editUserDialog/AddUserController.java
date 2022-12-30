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
        if (dialogFieldsController.validateInput(true)) {
            try {
                authenticationService.registerUser(dialogFieldsController.firstName.getText(),
                        dialogFieldsController.lastName.getText(),
                        dialogFieldsController.email.getText(),
                        dialogFieldsController.password.getText(),
                        dialogFieldsController.roleChoiceBox.getValue());
            } catch (IllegalArgumentException e) {
                dialogFieldsController.warningMessage.setText(e.getMessage());
                return;
            }

            confirmed = true;
            stage.close();
        }
    }
}
