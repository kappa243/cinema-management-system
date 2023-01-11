package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import io.github.palexdev.materialfx.controls.MFXButton;
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
    protected MFXButton confirmButton;

    @FXML
    private VBox showDialogFields;
    @FXML
    protected ShowDialogFieldController showDialogFieldsController;

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
        if (show.getMovie() != null) {
            showDialogFieldsController.movieComboBox.selectItem(show.getMovie());
            showDialogFieldsController.movieComboBox.setText(show.getMovie().getTitle());
        }
        if (show.getRoom() != null) {
            showDialogFieldsController.roomComboBox.selectItem(show.getRoom());
            showDialogFieldsController.roomComboBox.setText(show.getRoom().getRoomName());
        }
        showDialogFieldsController.setStartTime(show.getStartTime());
        showDialogFieldsController.setEndTime(show.getEndTime());
        showDialogFieldsController.setSellTicketsFrom(show.getSellTicketsFrom());
        showDialogFieldsController.ticketPrice.setText(Double.toString(show.getTicketPrice()));
        showDialogFieldsController.soldTickets.setText(Integer.toString(show.getSoldTickets()));
    }

    protected void updateModel() {
        show.setMovie(showDialogFieldsController.movieComboBox.getValue());
        show.setRoom(showDialogFieldsController.roomComboBox.getValue());
        show.setStartTime(showDialogFieldsController.getStartTime());
        show.setEndTime(showDialogFieldsController.getEndTime());
        show.setSellTicketsFrom(showDialogFieldsController.getSellTicketsFrom());
        show.setTicketPrice(Double.parseDouble(showDialogFieldsController.ticketPrice.getText()));
        show.setSoldTickets(Integer.parseInt(showDialogFieldsController.soldTickets.getText()));
    }

    @FXML
    protected void initialize() {}

    protected void handleConfirmAction(ActionEvent event) {
        if (showDialogFieldsController.validateInput()) {
            updateModel()
            ;
            confirmed = true;
            stage.close();
        }
    }
}
