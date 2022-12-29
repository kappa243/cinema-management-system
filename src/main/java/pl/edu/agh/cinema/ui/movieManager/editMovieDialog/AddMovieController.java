package pl.edu.agh.cinema.ui.movieManager.editMovieDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.movie.MovieService;

@Component
@Scope("prototype")
public class AddMovieController extends CommonMovieController{

    private MovieService movieService;

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
        super.handleConfirmAction(event);
        movieService.addMovie(movie);
    }
}
