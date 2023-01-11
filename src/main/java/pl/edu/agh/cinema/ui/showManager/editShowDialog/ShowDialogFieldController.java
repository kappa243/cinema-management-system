package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXComboBoxCell;
import io.github.palexdev.virtualizedfx.cell.Cell;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.movie.MovieService;
import pl.edu.agh.cinema.model.room.Room;
import pl.edu.agh.cinema.model.room.RoomService;
import pl.edu.agh.cinema.ui.StageAware;

import java.time.LocalDateTime;


@Component
@Scope("prototype")
public class ShowDialogFieldController implements StageAware {

    @Setter
    private Stage stage;

    @FXML
    public MFXComboBox<Movie> movieComboBox;

    @FXML
    public HBox startTime;
    public DateTimeInputController startTimeController;
    @FXML
    public HBox endTime;
    public DateTimeInputController endTimeController;
    @FXML
    public HBox sellTicketsFrom;
    public DateTimeInputController sellTicketsFromController;

    @FXML
    public MFXComboBox<Room> roomComboBox;


    @FXML
    public MFXTextField ticketPrice;

    @FXML
    public MFXTextField soldTickets;
    @FXML
    private Label warningMessage;
    private MovieService movieService;
    private RoomService roomService;

    public ShowDialogFieldController(MovieService movieService, RoomService roomService) {
        this.movieService = movieService;
        this.roomService = roomService;
    }


    public boolean validateInput() {
        if (movieComboBox.getValue() == null) {
            warningMessage.setText("Movie not set!");
            return false;
        }
        if (roomComboBox.getValue() == null) {
            warningMessage.setText("Room not set!");
            return false;
        }
        if (!startTimeController.validateInput(warningMessage)) {
            return false;
        }
        if (!endTimeController.validateInput(warningMessage)) {
            return false;
        }
        if (!sellTicketsFromController.validateInput(warningMessage)) {
            return false;
        }
        if (!ticketPrice.getText().matches("\\d+(\\.[0-9]{0,2}){0,1}")) {
            warningMessage.setText("Ticket price must be a number (max 2 digits after dot)!");
            return false;
        }
        if (!soldTickets.getText().matches("\\d+")) {
            warningMessage.setText("Sold tickets must be a number!");
            return false;
        }
        if (!getEndTime().isAfter(getStartTime())) {
            warningMessage.setText("Start time must be before end time!");
            return false;
        }
        if(!(movieComboBox.getSelectionModel().getSelectedItem() == null)) {
            if(!getStartTime().isAfter(movieComboBox.getSelectionModel().getSelectedItem().getReleaseDate())){
                warningMessage.setText("Start time must be after release date!");
                return false;
            }
        }
        if (!getStartTime().isAfter(getSellTicketsFrom())) {
            warningMessage.setText("Selling tickets must start before the show!");
            return false;
        }
        return true;
    }

    public void setStartTime(LocalDateTime time) {
        startTimeController.setDateTime(time);
    }

    public void setEndTime(LocalDateTime time) {
        endTimeController.setDateTime(time);
    }

    public void setSellTicketsFrom(LocalDateTime time) {
        sellTicketsFromController.setDateTime(time);
    }

    public LocalDateTime getStartTime() {
        return startTimeController.getDate();
    }

    public LocalDateTime getEndTime() {
        return endTimeController.getDate();
    }

    public LocalDateTime getSellTicketsFrom() {
        return sellTicketsFromController.getDate();
    }

    @FXML
    private void initialize() {
        movieComboBox.setItems(movieService.getMovies());
        movieComboBox.setConverter(new StringConverter<Movie>() {
            @Override
            public String toString(Movie movie) {
                if (movie == null) return "";
                else return movie.getTitle();
            }

            @Override
            public Movie fromString(String string) {
                return movieComboBox.getItems().stream().filter(m -> m.getTitle().equals(string))
                        .findAny()
                        .orElse(null);
            }
        });

        roomComboBox.setItems(roomService.getRooms());
        roomComboBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room room) {
                if (room == null) return "";
                return room.getRoomName();
            }

            @Override
            public Room fromString(String string) {
                return roomComboBox.getItems().stream().filter(i -> i.getRoomName().equals(string))
                        .findAny()
                        .orElse(null);
            }
        });

    }


}
