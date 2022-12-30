package pl.edu.agh.cinema.ui.ticketsManager;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.ui.StageAware;

@Component
@Scope("prototype")
public class SellTicketsDialogController implements StageAware {

    @Setter
    private Stage stage;

    private Show show;
    @FXML
    private Button sellButton;

    @FXML
    private TextField ticketsNumber;

    @FXML
    private Label warningMessage;


    public void setData(Show show) {
        this.show = show;
    }

    @FXML
    public void initialize() {
        ticketsNumber.setText("1");
        sellButton.setOnAction(this::handleSellAction);
    }
    public boolean validateInput() {
        if (!ticketsNumber.getText().matches("\\d+")) {
            warningMessage.setText("Must be a number!");
            return false;
        } else {
            int numberToSell = Integer.parseInt(ticketsNumber.getText());
            if (numberToSell + show.getSoldTickets() > show.getRoom().getSeatsNumber()) {
                warningMessage.setText("Not enough seats available!");
                return false;
            }
        }
        return true;
    }
    public void handleSellAction(Event event) {
        if (validateInput()) {
            show.setSoldTickets(show.getSoldTickets() + Integer.parseInt(ticketsNumber.getText()));
            stage.close();
        }
    }



}
