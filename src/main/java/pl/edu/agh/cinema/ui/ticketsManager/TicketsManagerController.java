package pl.edu.agh.cinema.ui.ticketsManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import javafx.beans.binding.Bindings;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.StageManager;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.room.Room;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.model.show.ShowService;
import pl.edu.agh.cinema.ui.StageAware;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class TicketsManagerController implements StageAware {

    private ApplicationEventPublisher publisher;
    private StageManager stageManager;
    private ViewManager viewManager;

    private ShowService showService;

    @FXML
    private MFXButton sellButton;

    @FXML
    private TableView<Show> showsTable;

    @FXML
    private TableColumn<Show, Room> roomColumn;

    @FXML
    private TableColumn<Show, Movie> movieColumn;

    @FXML
    private TableColumn<Show, LocalDateTime> startTimeColumn;

    @FXML
    private TableColumn<Show, LocalDateTime> endTimeColumn;

    @FXML
    private TableColumn<Show, LocalDateTime> sellTicketsFromColumn;
    @FXML
    private TableColumn<Show, Integer> ticketPriceColumn;

    @FXML
    private TableColumn<Show, Integer> soldTicketsColumn;
    @FXML
    private TableColumn<Show, Room> maxSeatsColumn;
    @FXML
    private MFXTextField queryField;

    @Setter
    private Stage stage;


    public TicketsManagerController(ApplicationEventPublisher publisher,
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
        showsTable.setItems(showService.getShows().filtered(show -> {
            return show.getStartTime().isAfter(LocalDateTime.now()) &&
                    show.getSellTicketsFrom().isBefore(LocalDateTime.now());
        }));


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
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("movie")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        startTimeColumn.setCellFactory(column -> {
            TableCell<Show, LocalDateTime> cell = new TableCell<Show, LocalDateTime>() {
                private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
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
            TableCell<Show, LocalDateTime> cell = new TableCell<Show, LocalDateTime>() {
                private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
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
        sellTicketsFromColumn.setCellFactory(column -> {
            TableCell<Show, LocalDateTime> cell = new TableCell<Show, LocalDateTime>() {
                private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
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

        sellTicketsFromColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
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

        maxSeatsColumn.setCellFactory(column -> {
            TableCell<Show, Room> cell = new TableCell<Show, Room>() {

                @Override
                protected void updateItem(Room item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(Integer.toString(item.getSeatsNumber()));
                    }
                }
            };
            return cell;
        });
        maxSeatsColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("room")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        sellButton.setOnAction(this::handleSellAction);
        sellButton.disableProperty().bind(
                Bindings.size(showsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
        );

        queryField.setOnKeyPressed(e -> this.setItems());
        MFXTooltip.of(
                queryField,
                "You can search multiple queries by separating them with a space"
        ).install();
    }

    public void setItems() {
        showsTable.setItems(showService.getShows().filtered(show -> {
            return show.getStartTime().isAfter(LocalDateTime.now()) &&
                    show.getSellTicketsFrom().isBefore(LocalDateTime.now());
        }).filtered(show -> {
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


    public void handleSellAction(Event event) {
        try {
            Stage stage = new Stage();
            Pair<Parent, SellTicketsDialogController> vmLoad = viewManager.load("/fxml/ticketsManager/sellTicketsDialog.fxml", stage);
            Parent parent = vmLoad.getFirst();
            SellTicketsDialogController controller = vmLoad.getSecond();


            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));
            stage.setTitle("Sell tickets");

            Show show = showsTable.getSelectionModel().getSelectedItem();
            controller.setData(show);
            controller.setStage(stage);

            stage.showAndWait();
            setItems();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
