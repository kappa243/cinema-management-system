package pl.edu.agh.cinema.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class AddUserController implements Initializable {

    @FXML
    private Button addUserButton;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;

    public  AddUserController() {}



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addUserButton.setOnAction(event -> {
            System.out.println(firstName.getText() + " " + lastName.getText() + " " + email.getText());
        });

    }
}
