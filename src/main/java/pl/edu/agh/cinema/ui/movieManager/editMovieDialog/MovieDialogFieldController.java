package pl.edu.agh.cinema.ui.movieManager.editMovieDialog;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ui.StageAware;

import java.io.File;

@Component
@Scope("prototype")
public class MovieDialogFieldController implements StageAware {

    @Setter
    public Stage stage;

    @FXML
    public MFXTextField title;
    @FXML
    public MFXTextField description;
    @FXML
    public MFXDatePicker releaseDate;

    @FXML
    public MFXButton imageButton;

    @FXML
    public ImageView imageView;

    @FXML
    public Label warningMessage;


    public File selectedFile;

    @FXML
    public void initialize() {
        imageButton.setOnAction(e -> this.setImage());
    }

    private void setImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg, *.png)", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            imageView.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
        }
    }

    public boolean validateInput(boolean updateImage) {
        if (title.getText().isEmpty()) {
            warningMessage.setText("Title cannot be empty");
            return false;
        }

        if (description.getText().isEmpty()) {
            warningMessage.setText("Description cannot be empty");
            return false;
        }

        if (releaseDate.getValue() == null) {
            warningMessage.setText("Release date cannot be empty");
            return false;
        }

        if (updateImage && selectedFile == null) {
            warningMessage.setText("Choose correct cover image");
            return false;
        }

        warningMessage.setText("");
        return true;
    }
}
