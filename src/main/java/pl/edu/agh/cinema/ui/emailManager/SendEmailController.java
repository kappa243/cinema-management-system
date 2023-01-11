package pl.edu.agh.cinema.ui.emailManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.mail.EmailService;
import pl.edu.agh.cinema.ui.StageAware;

import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;

@Component
@Scope("prototype")
public class SendEmailController implements StageAware {

    @Autowired
    private EmailService emailService;

    @FXML
    private TextField subject;

    @FXML
    private TextArea text;
    @FXML
    private Button sendButton;
    @FXML
    private Label warningMessage;
    @FXML
    private Button attachButton;
    @FXML
    private Label attach;

    private Boolean warned = false;

    @Setter
    private Stage stage;

    @Setter
    private ArrayList<String> emails;
    private final ArrayList<File> attachments = new ArrayList<>();

    @FXML
    public void initialize() {
        warningMessage.setText("");
        sendButton.setOnAction(this::send);
        attachButton.setOnAction(this::setFile);
    }
    public void setFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            attachments.add(file);
            attach.setText(attach.getText().concat(file.getName() + " "));
        }
    }
    public void send(ActionEvent event) {
        if (!warned) {
            if (subject.getText().isEmpty()) {
                warningMessage.setText("No subject!\n");
                warned = true;
            }
            if (text.getText().isEmpty()) {
                warningMessage.setText(warningMessage.getText().concat("Do you want to send an empty message?"));
                warned = true;
            }
            if (warned) return;
        }

        warned = false;
        warningMessage.setText("");
        if (!attachments.isEmpty()) {
            try {
                emailService.sendMessageWithAttachment(emails, subject.getText(), text.getText(), attachments);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } else emailService.sendSimpleMessage(emails, subject.getText(), text.getText());
        stage.close();

    }
}