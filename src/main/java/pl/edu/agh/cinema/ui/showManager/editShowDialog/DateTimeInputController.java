package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ui.StageAware;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Scope("prototype")
public class DateTimeInputController implements StageAware {
    @Setter
    private Stage stage;

    @FXML
    public DatePicker date;

    @FXML
    public TextField hour;

    @FXML
    public TextField minute;

    public void setDateTime(LocalDateTime time) {
        date.setValue(time.toLocalDate());
        hour.setText(Integer.toString(time.getHour()));
        minute.setText(Integer.toString(time.getMinute()));
    }
    public LocalDateTime getDate() {
        LocalTime time = LocalTime.of(Integer.parseInt(hour.getText()),
                Integer.parseInt(minute.getText()));
        return LocalDateTime.of(date.getValue(), time);
    }
    public boolean validateInput(Label warningMessage) {
        if (date.getValue() == null) {
            warningMessage.setText("Date not set!");
            return false;
        }
        if (hour.getText().matches("\\d+")) {
            int h = Integer.parseInt(hour.getText());
            if (h < 0 || h > 23) {
                warningMessage.setText("Hour must be a number between 0 and 23!");
                return false;
            }
        }
        else {
            warningMessage.setText("Hour must be a number between 0 and 23!");
            return false;
        }
        if (minute.getText().matches("\\d+")) {
            int h = Integer.parseInt(minute.getText());
            if (h < 0 || h > 59) {
                warningMessage.setText("Minute must be a number between 0 and 59!");
                return false;
            }
        }
        else {
            warningMessage.setText("Minute must be a number between 0 and 59!");
            return false;
        }
        return true;
    }
}
