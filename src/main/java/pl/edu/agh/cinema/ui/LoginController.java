package pl.edu.agh.cinema.ui;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.auth.AuthenticationService;
import pl.edu.agh.cinema.ui.userManager.UserManagerController;

import java.io.IOException;


@Component
@Scope("prototype")
public class LoginController implements StageAware {

    private final AuthenticationService authenticationService;
    private final ViewManager viewManager;

    @FXML
    private MFXTextField email;

    @FXML
    private MFXPasswordField password;

    @FXML
    private MFXButton loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private MFXButton bypassLogin;

    @Setter
    private Stage stage;

    public LoginController(AuthenticationService authenticationService, ViewManager viewManager) {
        this.authenticationService = authenticationService;
        this.viewManager = viewManager;
    }


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            stage.setResizable(false);

            email.requestFocus();
        });
        errorLabel.setVisible(false);

        loginButton.setOnAction(event -> {
            try {
                login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        bypassLogin.setOnAction(event -> {
            email.setText("admin");
            password.setText("admin");

            try {
                login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void login() throws IOException {
        String email = this.email.getText();
        String password = this.password.getText();

        try {
            authenticationService.login(email, password);
        } catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }

        if (authenticationService.isAuthenticated()) {
            // clean fields
            this.email.setText("");
            this.password.setText("");


            // close login window
            Stage stage = new Stage();
            Pair<Parent, UserManagerController> vmLoad = viewManager.load("/fxml/main.fxml", stage);

            stage.setScene(new Scene(vmLoad.getFirst()));
            stage.setResizable(false);
            stage.setTitle("Cinema");
            stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));

            stage.show();

            this.stage.close();
        }
    }
}
