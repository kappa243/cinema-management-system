package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.ui.StageAware;


abstract class CommonShowController implements StageAware {
    @Setter
    protected Stage stage;

    @FXML
    protected Button confirmButton;

    @FXML
    private VBox showDialogField;
    @FXML
    protected ShowDialogFieldController showDialogFieldController;

    protected Show show;

    protected boolean confirmed = false;

    public void setData(Show show) {
        this.show = show;
        updateContent();
    }

    /**
     * @return Returns true if the user clicked OK, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    protected void updateContent() {
        showDialogFieldController.movieComboBox.setValue(show.getMovie());
        showDialogFieldController.roomComboBox.setValue(show.getRoom());
        showDialogFieldController.setStartTime(show.getStartTime());
        showDialogFieldController.setEndTime(show.getEndTime());
        showDialogFieldController.setSellTicketsFrom(show.getSellTicketsFrom());
        showDialogFieldController.ticketPrice.setText(Double.toString(show.getTicketPrice()));
        showDialogFieldController.soldTickets.setText(Integer.toString(show.getSoldTickets()));
    }

    protected void updateModel() {
        show.setMovie(showDialogFieldController.movieComboBox.getValue());
        show.setRoom(showDialogFieldController.roomComboBox.getValue());
        show.setStartTime(showDialogFieldController.getStartTime());
        show.setEndTime(showDialogFieldController.getEndTime());
        show.setSellTicketsFrom(showDialogFieldController.getSellTicketsFrom());
        show.setTicketPrice(Double.parseDouble(showDialogFieldController.ticketPrice.getText()));
        show.setSoldTickets(Integer.parseInt(showDialogFieldController.soldTickets.getText()));
    }

    @FXML
    protected void initialize() {}

    protected void handleConfirmAction(ActionEvent event) {
        if (showDialogFieldController.validateInput()) {
            updateModel();
            confirmed = true;
            stage.close();
        }
    }
}
