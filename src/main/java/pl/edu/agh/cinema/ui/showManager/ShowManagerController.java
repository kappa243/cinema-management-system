package pl.edu.agh.cinema.ui.showManager;


import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

@Component
@Scope("prototype")
public class ShowManagerController implements StageAware {
    private ApplicationEventPublisher publisher;
    private StageManager stageManager;
    private ViewManager viewManager;
    private ShowService showService;
    @Setter
    private Stage stage;

    @FXML
    private TableView<Show> showsTable;

    @FXML
    private TableColumn<Show, Movie> movieColumn;
    @FXML
    private TableColumn<Room, Movie> roomColumn;

    @FXML
    private TableColumn<Show, Timestamp> startTimeColumn;

    @FXML
    private TableColumn<Show, Timestamp> endTimeColumn;

    @FXML
    private TableColumn<Show, Timestamp> sellTicketsFromColumn;

    @FXML
    private TableColumn<Show, Double> ticketPriceColumn;

    @FXML
    private TableColumn<Show, Integer> soldTicketsColumn;


    public ShowManagerController(ApplicationEventPublisher publisher,
                                 StageManager stageManager,
                                 ViewManager viewManager,
                                 ShowService showService) {
        this.publisher = publisher;
        this.stageManager = stageManager;
        this.viewManager = viewManager;
        this.showService = showService;
    }

    @FXML
    public void initialize() {
        setItems();
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
        startTimeColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("startTime")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        endTimeColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("endTime")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        sellTicketsFromColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("sellTicketsFrom")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        ticketPriceColumn.setCellValueFactory(cellData -> {
            try {
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
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("soldTickets")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void setItems() {
        showsTable.setItems(showService.getShows());
    }

}
