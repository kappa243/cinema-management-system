package pl.edu.agh.cinema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.person.Person;

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

    @Setter
    protected Stage stage;

    private Person person;

    private boolean confirmed = false;


    public void setData(Person person) {
        this.person = person;
        updateContent();
    }

    /**
     * @return Returns true if the user clicked OK, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    private void updateModel() {
        person.setFirstName(firstName.getText());
        person.setLastName(lastName.getText());
        person.setEmail(email.getText());
    }

    private void updateContent() {
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        email.setText(person.getEmail());
    }

    @FXML
    public void initialize() {
        confirmButton.setOnAction(this::handleConfirmAction);
    }

    // TODO - temporary solution
    public void renameButton(String text){
        confirmButton.setText(text);
    }

    private void handleConfirmAction(ActionEvent event) {
        updateModel();
        confirmed = true;

        stage.close();
    }
}
