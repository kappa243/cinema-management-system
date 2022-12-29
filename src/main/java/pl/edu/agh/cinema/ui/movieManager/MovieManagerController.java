package pl.edu.agh.cinema.ui.movieManager;

import javafx.beans.binding.Bindings;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import pl.edu.agh.cinema.model.movie.MovieService;
import pl.edu.agh.cinema.ui.StageAware;
import pl.edu.agh.cinema.ui.movieManager.editMovieDialog.AddMovieController;
import pl.edu.agh.cinema.ui.movieManager.editMovieDialog.EditMovieController;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class MovieManagerController implements StageAware {

    private ViewManager viewManager;
    private MovieService movieService;
    @Setter
    private Stage stage;

    @FXML
    private TableView<Movie> moviesTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> descriptionColumn;
    @FXML
    private TableColumn<Movie, Date> releaseDateColumn;
    @FXML
    private Button addNewMovieButton;
    @FXML
    private Button editMovieButton;
    @FXML
    private Button deleteMovieButton;

    @FXML
    private TextField queryField;

    public MovieManagerController(ViewManager viewManager,
                                  MovieService movieService) {
        this.viewManager = viewManager;
        this.movieService = movieService;
    }

    @FXML
    public void initialize() {
        moviesTable.setItems(movieService.getMovies());

        titleColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanStringPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("title")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        descriptionColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanStringPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("description")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        releaseDateColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("releaseDate")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        editMovieButton.disableProperty().bind(
                Bindings.size(moviesTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
        );
        editMovieButton.setOnAction(this::handleEditAction);
        addNewMovieButton.setOnAction(this::handleAddAction);
        deleteMovieButton.setOnAction(this::handleDeleteAction);

        queryField.setOnKeyTyped(e -> this.setItems());
    }

    public void setItems() {
        moviesTable.setItems(movieService.getMovies().filtered(movie -> {
            List<String> queries = new ArrayList<>(List.of(queryField.getText().split(" ")));

            // remove empty queries
            queries.removeIf(String::isEmpty);

            if (queries.isEmpty()) {
                return true;
            }

            return queries.stream().allMatch(query -> {
                String lowerCaseQuery = query.toLowerCase();
                return movie.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        movie.getDescription().toLowerCase().contains(lowerCaseQuery);
            });

        }));
    }

    private void handleAddAction(ActionEvent event) {
        try {
            Stage stage = new Stage();

            Pair<Parent, AddMovieController> vmLoad = viewManager.load("/fxml/movieManager/editMovieDialog/addMovie.fxml", stage);
            Parent parent = vmLoad.getFirst();
            AddMovieController controller = vmLoad.getSecond();

            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));
            stage.setTitle("Add new movie");

            Movie movie = new Movie("", "", Date.valueOf(LocalDate.now()));
            controller.setData(movie);
            controller.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Pair<Parent, EditMovieController> vmLoad = viewManager.load("/fxml/movieManager/editMovieDialog/editMovie.fxml", stage);
            Parent parent = vmLoad.getFirst();
            EditMovieController controller = vmLoad.getSecond();


            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.getIcons().add(new javafx.scene.image.Image("/static/img/app-icon.png"));
            stage.setTitle("Edit movie");

            Movie movie = moviesTable.getSelectionModel().getSelectedItem();
            controller.setData(movie);
            controller.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAction(ActionEvent event) {
        Movie movie = moviesTable.getSelectionModel().getSelectedItem();
        movieService.deleteMovie(movie);
    }
}

