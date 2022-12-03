package pl.edu.agh.cinema.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ApplicationCloseEvent;
import pl.edu.agh.cinema.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ApplicationController implements Initializable {

    ApplicationEventPublisher publisher;
    ViewManager viewManager;

    @FXML
    private Button exitButton;

    @FXML
    private Button addNewUserButton;

    @FXML
    private void initialize() {
    }


    public ApplicationController(ApplicationEventPublisher publisher, ViewManager viewManager) {
        this.publisher = publisher;
        this.viewManager = viewManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitButton.setOnAction(event -> {
            publisher.publishEvent(new ApplicationCloseEvent(this));
        });
        addNewUserButton.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Parent parent = viewManager.load("/fxml/addUser.fxml");
                stage.setScene(new Scene(parent));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
