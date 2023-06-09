package pl.edu.agh.cinema.ui.showManager.editShowDialog;

import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EditShowController extends CommonShowController {

    @FXML
    @Override
    protected void initialize() {
        super.initialize();
        confirmButton.setOnAction(this::handleConfirmAction);
    }
}
