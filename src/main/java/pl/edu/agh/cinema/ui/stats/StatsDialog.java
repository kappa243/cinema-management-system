package pl.edu.agh.cinema.ui.stats;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.bytebuddy.asm.Advice;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.movie.MovieService;
import pl.edu.agh.cinema.model.sales.SalesService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

public class StatsDialog {

    Label movieLabel = new Label(" Select movie: ");
    Label timeLabel = new Label(" Select time range: ");
    Label dataLabel = new Label(" Select type of data: ");

    ComboBox<Movie> movieComboBox=new ComboBox<>();
    ComboBox<String> timeComboBox=new ComboBox<>();
    ComboBox<String> dataComboBox=new ComboBox<>();

    Button applyButton = new Button("Apply");
    Button closeButton = new Button("Close");

    VBox layout =new VBox();
    GridPane gridPane =new GridPane();

    MovieService movieService;
    SalesService salesService;

    LineChart<String,Number> salesChart=new LineChart<String,Number>(new CategoryAxis(), new NumberAxis());

    static int width=80;
    static int height=60;
    static double startEnergy=200;
    static double moveEnergy=5;
    static double plantEnergy=200;
    static double jungleRatio=0.5;
    static int initialNumberOfAnimals=100;
    static boolean returnValue=false;
    static boolean nwMapMagic=false;
    static boolean wMapMagic=false;

    public StatsDialog(MovieService movieService, SalesService salesService){
        this.movieService=movieService;
        this.salesService=salesService;

    }

    void initialize(){
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().add(gridPane);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(movieLabel, 0,1);
        gridPane.add(timeLabel, 0,2);
        gridPane.add(dataLabel, 0,3);

        ObservableList<Movie> movies = movieService.getMovies();
        movies.add(0,null);
        movieComboBox.setItems(movies);
        movieComboBox.getSelectionModel().select(0);
        movieComboBox.setCellFactory(cellData -> {
            ListCell<Movie> cell = new ListCell<Movie>() {
                @Override
                protected void updateItem(Movie item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("All movies");
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
                if (movie == null) return "All movies";
                else return movie.getTitle();
            }

            @Override
            public Movie fromString(String string) {
                return movieComboBox.getItems().stream().filter(m -> m.getTitle().equals(string))
                        .findAny()
                        .orElse(null);
            }
        });

        gridPane.add(movieComboBox, 1,1);

        timeComboBox.getItems().addAll("Last week", "Last month");
        timeComboBox.getSelectionModel().select(0);
        gridPane.add(timeComboBox, 1,2);

        dataComboBox.getItems().addAll("Tickets", "Value of tickets");
        dataComboBox.getSelectionModel().select(0);
        gridPane.add(dataComboBox, 1,3);

        applyButton.setOnAction(e->onApply());
        gridPane.add(applyButton,1,4);

        salesChart.setStyle("-fx-pref-width: 360;-fx-alignment:center;-fx-font-size: 16px;-fx-font-family: \"Arial Black\";-fx-text-fill:white;-fx-padding: 10;-fx-border-width: 2");
        salesChart.getXAxis().setAnimated(false);
        layout.getChildren().add(salesChart);
    }



    public void display() {
        initialize();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(layout, 700, 600);
        stage.setTitle("Statistics");
        stage.setScene(scene);
        stage.showAndWait();
    }

    void onApply(){
        Movie m=movieComboBox.getValue();

        String dateRange=timeComboBox.getValue();
        String typeOfData=dataComboBox.getValue();

        LocalDateTime endTime=LocalDateTime.now();
        LocalDateTime startTime=endTime.minusDays(dateRange.equals("Last week")?7:31);

        Map<LocalDateTime, Integer> map;
        if(m!=null){
            if(typeOfData.equals("Tickets")){
                map=salesService.getSoldTickets(startTime, endTime, m);
            }
            else{
                map=salesService.getSoldTicketsValue(startTime, endTime, m);
            }
        }
        else{
            if(typeOfData.equals("Tickets")){
                map=salesService.getSoldTickets(startTime, endTime);
            }
            else{
                map=salesService.getSoldTicketsValue(startTime, endTime);
            }
        }


//        StringBuilder b=new StringBuilder();
//        for(var e:map.entrySet()){
//            b.append(e.getKey());
//            b.append(" ");
//            b.append(e.getValue());
//            b.append("\n");
//        }
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Movie: " + m + " dateRange:"+dateRange+" type: "+typeOfData, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, b.toString(), ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
//        alert.showAndWait();

        salesChart.getData().clear();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName(m==null?"All movies":m.getTitle());
        for(var k:map.entrySet()){
            String formattedDateTime = k.getKey().format(df);
            series.getData().add(new XYChart.Data(formattedDateTime, k.getValue()));
        }
        salesChart.getData().add(series);
    }

}
