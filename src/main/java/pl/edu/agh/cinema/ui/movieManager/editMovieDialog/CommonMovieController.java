package pl.edu.agh.cinema.ui.movieManager.editMovieDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.ui.StageAware;
import pl.edu.agh.cinema.utils.ImageConverter;

import java.io.IOException;
import java.sql.Date;

abstract class CommonMovieController implements StageAware {

    @FXML
    protected Button confirmButton;

    @FXML
    private VBox movieDialogFields;
    @FXML
    protected MovieDialogFieldController movieDialogFieldsController;

    @Setter
    protected Stage stage;

    protected Movie movie;

    protected boolean confirmed = false;


    public void setData(Movie movie) {
        this.movie = movie;
        updateContent();
    }

    /**
     * @return Returns true if the user clicked OK, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    protected void updateContent() {
        if (movie.getCover() != null) {
            movieDialogFieldsController.imageView.setImage(ImageConverter.byteToImage(movie.getCover()));
        }
        movieDialogFieldsController.title.setText(movie.getTitle());
        movieDialogFieldsController.description.setText(movie.getDescription());
        movieDialogFieldsController.releaseDate.setValue(movie.getReleaseDate().toLocalDate());
    }

    protected void updateModel() throws IOException {
        if (movieDialogFieldsController.selectedFile != null) {
            movie.setCover(ImageConverter.fileToByte(movieDialogFieldsController.selectedFile));
        }
        movie.setTitle(movieDialogFieldsController.title.getText());
        movie.setDescription(movieDialogFieldsController.description.getText());
        movie.setReleaseDate(movieDialogFieldsController.releaseDate.getValue().atStartOfDay());
    }

    @FXML
    protected void initialize() {
    }

    protected void handleConfirmAction(ActionEvent event) {
        boolean updateImage = movieDialogFieldsController.selectedFile != null;
        if (movieDialogFieldsController.validateInput(updateImage)) {
            try {
                updateModel();
            } catch (IOException e) {
                e.printStackTrace();
                movieDialogFieldsController.warningMessage.setText("Error while reading image");
                return;
            }
            confirmed = true;
            stage.close();
        }
    }

}
