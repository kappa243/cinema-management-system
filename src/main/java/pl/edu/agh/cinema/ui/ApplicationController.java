package pl.edu.agh.cinema.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ApplicationCloseEvent;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ApplicationController implements Initializable {

    ApplicationEventPublisher publisher;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;


    @FXML
    private Button addButton;

    @FXML
    private void initialize() {
    }


    public ApplicationController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteButton.setOnAction(event -> {
            publisher.publishEvent(new ApplicationCloseEvent(this));
        });
    }

}
