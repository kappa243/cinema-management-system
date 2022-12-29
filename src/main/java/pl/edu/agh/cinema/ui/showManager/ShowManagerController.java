package pl.edu.agh.cinema.ui.showManager;

import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.StageManager;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.room.Room;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.model.show.ShowService;
import pl.edu.agh.cinema.ui.StageAware;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ShowManagerController implements StageAware {


    ApplicationEventPublisher publisher;
    StageManager stageManager;
    ViewManager viewManager;

    ShowService showService;

    @FXML
    private Button addNewShowButton;

    @FXML
    private Button editShowButton;

    @FXML
    private Button deleteShowButton;

    @FXML
    private TableView<Show> showsTable;

    @FXML
    private TableColumn<Show, Room> roomColumn;

    @FXML
    private TableColumn<Show, Movie> movieColumn;

    @FXML
    private TableColumn<Show, Timestamp> startTimeColumn;

    @FXML
    private TableColumn<Show, Timestamp> endTimeColumn;

    @FXML
    private TableColumn<Show, Integer> ticketPriceColumn;

    @FXML
    private TableColumn<Show, Integer> soldTicketsColumn;

    @FXML
    private TextField queryField;

    @Setter
    private Stage stage;


    public ShowManagerController(ApplicationEventPublisher publisher,
                                 StageManager stageManager,
                                 ViewManager viewManager,
                                 ShowService showService) {
        this.publisher = publisher;
        this.viewManager = viewManager;
        this.stageManager = stageManager;

        this.showService = showService;
    }

    @FXML
    public void initialize() {
        showsTable.setItems(showService.getShows());


        roomColumn.setCellFactory(column -> {
            TableCell<Show, Room> cell = new TableCell<Show, Room>() {

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

        roomColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("room")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        movieColumn.setCellFactory(column -> {
            TableCell<Show, Movie> cell = new TableCell<Show, Movie>() {

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
        movieColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("movie")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        startTimeColumn.setCellFactory(column -> {
            TableCell<Show, Timestamp> cell = new TableCell<Show, Timestamp>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");

                @Override
                protected void updateItem(Timestamp item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });
        startTimeColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("startTime")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        endTimeColumn.setCellFactory(column -> {
            TableCell<Show, Timestamp> cell = new TableCell<Show, Timestamp>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");

                @Override
                protected void updateItem(Timestamp item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });

        endTimeColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("endTime")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        ticketPriceColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("ticketPrice")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        soldTicketsColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("soldTickets")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        queryField.setOnKeyTyped(e -> this.setItems());

    }

    public void setItems() {
        showsTable.setItems(showService.getShows().filtered(show -> {
            List<String> queries = new ArrayList<>(List.of(queryField.getText().split(" ")));

            // remove empty queries
            queries.removeIf(String::isEmpty);

            if (queries.isEmpty()) {
                return true;
            }

            return queries.stream().allMatch(query -> {
                String lowerCaseQuery = query.toLowerCase();
                return show.getMovie().getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        show.getRoom().getRoomName().toLowerCase().contains(lowerCaseQuery);
            });

        }));
    }
}
