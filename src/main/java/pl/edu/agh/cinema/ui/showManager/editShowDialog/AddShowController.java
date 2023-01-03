package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.model.show.ShowService;

@Component
@Scope("prototype")
public class AddShowController extends CommonShowController {

    private ShowService showService;
    public AddShowController(ShowService showService) {
        this.showService = showService;
    }

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        confirmButton.setOnAction(this::handleConfirmAction);
    }

    @Override
    protected void handleConfirmAction(ActionEvent event) {
        if (showDialogFieldsController.validateInput()) {
            show = new Show();
            updateModel();
            showService.addShow(show);
            confirmed = true;
            stage.close();
        }
    }
}
