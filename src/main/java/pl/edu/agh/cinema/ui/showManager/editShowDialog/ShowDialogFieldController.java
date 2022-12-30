package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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

import java.sql.Timestamp;



@Component
@Scope("prototype")
public class ShowDialogFieldController implements StageAware {

    @Setter
    private Stage stage;

    @FXML
    public ComboBox<Movie> movieComboBox;

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
    public ComboBox<Room> roomComboBox;


    @FXML
    public TextField ticketPrice;

    @FXML
    public TextField soldTickets;
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
        if (!getEndTime().after(getStartTime())) {
            warningMessage.setText("Start time must be before end time!");
            return false;
        }
        if (!getStartTime().after(getSellTicketsFrom())) {
            warningMessage.setText("Selling tickets must start before the show!");
            return false;
        }
        return true;
    }

    public void setStartTime(Timestamp time) {
        startTimeController.setDateTime(time);
    }
    public void setEndTime(Timestamp time) {
        endTimeController.setDateTime(time);
    }
    public void setSellTicketsFrom(Timestamp time) {
        sellTicketsFromController.setDateTime(time);
    }

    public Timestamp getStartTime() {
        return startTimeController.getTimestamp();
    }
    public Timestamp getEndTime() {
        return endTimeController.getTimestamp();
    }
    public Timestamp getSellTicketsFrom() {
        return sellTicketsFromController.getTimestamp();
    }

    @FXML
    private void initialize() {
        movieComboBox.setItems(movieService.getMovies());
        movieComboBox.setCellFactory(cellData -> {
            ListCell<Movie> cell = new ListCell<Movie>() {
                @Override
                protected void updateItem(Movie item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getTitle());
                    }
                }
            };
            return cell;
        });
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
        roomComboBox.setCellFactory(cellData -> {
            ListCell<Room> cell = new ListCell<Room>() {
                @Override
                protected void updateItem(Room item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getRoomName());
                    }
                }
            };
            return cell;
        });
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
