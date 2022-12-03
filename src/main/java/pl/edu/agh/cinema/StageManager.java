package pl.edu.agh.cinema;

import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class StageManager {

    private final Application application;
    private final ViewManager viewManager;

    private final Property<Stage> primaryStage;

    public Optional<Stage> getPrimaryStage() {
        return Optional.ofNullable(primaryStage.getValue());
    }

    public Property<Stage> primaryStageProperty() {
        return primaryStage;
    }

    public StageManager(Application application, ViewManager viewManager) {
        this.application = application;
        this.viewManager = viewManager;

        this.primaryStage = new SimpleObjectProperty<Stage>(application, "primaryStage");
    }


    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        Stage stage = event.getStage();

        primaryStage.setValue(stage);

        try {
            Parent parent = viewManager.load("/fxml/main.fxml", stage);
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.show();
    }

    @EventListener
    public void onApplicationClose(ApplicationCloseEvent event) throws Exception {
        application.stop();
    }
}
