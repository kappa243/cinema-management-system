package pl.edu.agh.cinema;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ApplicationReadyEvent extends ApplicationEvent {

    @Getter
    private final Stage stage;

    public ApplicationReadyEvent(Object source, Stage stage) {
        super(source);
        this.stage = stage;
    }
}