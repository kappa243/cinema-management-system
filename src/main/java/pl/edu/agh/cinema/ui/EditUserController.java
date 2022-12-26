package pl.edu.agh.cinema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.user.User;
import pl.edu.agh.cinema.model.user.Role;

@Component
@Scope("prototype")
public class EditUserController implements StageAware {

    @FXML
    private Button confirmButton;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private ChoiceBox<Role> roleChoiceBox;
    @FXML
    private Label warningMessage;

    @Setter
    protected Stage stage;

    private User user;

    private boolean confirmed = false;


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

    private void updateModel() {
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        user.setRole(roleChoiceBox.getValue());
    }

    private void updateContent() {
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        roleChoiceBox.setValue(user.getRole());
    }

    @FXML
    public void initialize() {
        confirmButton.setOnAction(this::handleConfirmAction);
        roleChoiceBox.getItems().addAll(Role.ADMINISTRATOR, Role.MODERATOR, Role.ASSISTANT);
        roleChoiceBox.setValue(Role.ASSISTANT);
    }

    // TODO - temporary solution
    public void renameButton(String text){
        confirmButton.setText(text);
    }

    public boolean validateInput() {
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

    private void handleConfirmAction(ActionEvent event) {
        if (validateInput()) {
            updateModel();
            confirmed = true;
            stage.close();
        }
    }
}
