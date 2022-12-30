package pl.edu.agh.cinema.ui.movieManager.editMovieDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.movie.MovieService;

import java.io.IOException;

@Component
@Scope("prototype")
public class AddMovieController extends CommonMovieController{

    private final MovieService movieService;

    public AddMovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        confirmButton.setOnAction(this::handleConfirmAction);
    }

    @Override
    protected void handleConfirmAction(ActionEvent event) {
        if (movieDialogFieldsController.validateInput(true)) {
            try {
                updateModel();
            } catch (IOException e) {
                e.printStackTrace();
                movieDialogFieldsController.warningMessage.setText("Error while reading image");
                return;
            }

            movieService.addMovie(movie);

            confirmed = true;
            stage.close();
        }
    }
}
