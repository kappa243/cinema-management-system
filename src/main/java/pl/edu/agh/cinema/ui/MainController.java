package pl.edu.agh.cinema.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.StageManager;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.auth.AuthenticationService;
import pl.edu.agh.cinema.ui.showManager.ShowManagerController;
import pl.edu.agh.cinema.ui.ticketsManager.TicketsManagerController;
import pl.edu.agh.cinema.ui.userManager.UserManagerController;

@Component
@Scope("prototype")
public class MainController implements StageAware {

    private final AuthenticationService authenticationService;
    private final ViewManager viewManager;
    private final StageManager stageManager;

    @FXML
    private Button userManagerButton;

    @FXML
    private Button showManagerButton;

    @FXML
    private Button ticketManagerButton;

    @FXML
    private Button logoutButton;

    @Setter
    private Stage stage;

    public MainController(AuthenticationService authenticationService, ViewManager viewManager, StageManager stageManager) {
        this.authenticationService = authenticationService;
        this.viewManager = viewManager;
        this.stageManager = stageManager;
    }

    @FXML
    public void initialize() {
        // permissions
        Pane pane = (Pane) userManagerButton.getParent();
        if(!authenticationService.isAuthorized("users")){
            pane.getChildren().remove(userManagerButton);
        }
        if(!authenticationService.isAuthorized("movies")){
            pane.getChildren().remove(showManagerButton);
        }
        if(!authenticationService.isAuthorized("tickets")){
            pane.getChildren().remove(ticketManagerButton);
        }

        // events
        userManagerButton.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Pair<Parent, UserManagerController> vmLoad = viewManager.load("/fxml/userManager/userManager.fxml", stage);
                stage.setScene(new Scene(vmLoad.getFirst()));

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.stage);
                stage.setTitle("User management");
                stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));

                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        // TODO - implement movie management

        showManagerButton.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Pair<Parent, ShowManagerController> vmLoad = viewManager.load("/fxml/showManager/showManager.fxml", stage);
                stage.setScene(new Scene(vmLoad.getFirst()));

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.stage);
                stage.setTitle("Show management");
                stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));

                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // TODO - implement ticket sales
        ticketManagerButton.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Pair<Parent, TicketsManagerController> vmLoad = viewManager.load("/fxml/ticketsManager/ticketsManager.fxml", stage);
                stage.setScene(new Scene(vmLoad.getFirst()));

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.stage);
                stage.setTitle("Ticket sales management");
                stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));

                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logoutButton.setOnAction(event -> {
            authenticationService.logout();
            stageManager.getPrimaryStage().show();
            stage.close();
        });
    }

}
