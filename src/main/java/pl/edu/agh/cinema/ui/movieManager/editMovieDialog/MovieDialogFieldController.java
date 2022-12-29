package pl.edu.agh.cinema.ui.movieManager.editMovieDialog;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ui.StageAware;

@Component
@Scope("prototype")
public class MovieDialogFieldController implements StageAware {

    @Setter
    public Stage stage;

    @FXML
    public TextField title;
    @FXML
    public TextField description;
    @FXML
    public DatePicker releaseDate;

    @FXML
    public Label warningMessage;

}
