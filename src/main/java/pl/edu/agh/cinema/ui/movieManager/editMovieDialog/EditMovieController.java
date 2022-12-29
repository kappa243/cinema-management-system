package pl.edu.agh.cinema.ui.movieManager.editMovieDialog;

import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EditMovieController extends CommonMovieController{

    public EditMovieController() {

    }
    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        confirmButton.setOnAction(this::handleConfirmAction);
    }


}
