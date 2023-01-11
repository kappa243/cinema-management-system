package pl.edu.agh.cinema.ui.emailManager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.StageManager;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.mail.EmailService;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.model.show.ShowService;
import pl.edu.agh.cinema.model.user.Role;
import pl.edu.agh.cinema.model.user.User;
import pl.edu.agh.cinema.model.user.UserService;
import pl.edu.agh.cinema.ui.StageAware;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Scope("prototype")
public class ReceiverManagerController implements StageAware {

    @Setter
    private Stage stage;
    ApplicationEventPublisher publisher;
    StageManager stageManager;
    ViewManager viewManager;
    UserService userService;
    ShowService showService;
    @Autowired
    private EmailService mailSender;

    @Qualifier("recommendedShowsSimpleMessage")
    @Autowired
    private SimpleMailMessage recommendedShowsMessage;

    @FXML
    private TableView<User> receiversTable;
    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    @Enumerated(EnumType.STRING)
    private TableColumn<User, Role> roleColumn;
    @FXML
    private TableColumn<User, CheckBox> checkBoxColumn;

    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private Button sendEmailButton;
    @FXML
    private Label warningMessage;

    private final HashMap<User, CheckBox> checkBoxMap = new HashMap<>();

    @FXML
    private TextField queryField;
    @FXML
    private Button recommendedButton;


    public ReceiverManagerController(ApplicationEventPublisher publisher,
                                     StageManager stageManager,
                                     ViewManager viewManager,
                                     UserService userService,
                                     ShowService showService) {
        this.publisher = publisher;
        this.stageManager = stageManager;
        this.viewManager = viewManager;
        this.userService = userService;
        this.showService = showService;
    }

    @FXML
    public void initialize() {

        setItems();

        for (User user: userService.getUsers()) {
            checkBoxMap.put(user, new CheckBox());
        }

        firstNameColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanStringPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("firstName")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        lastNameColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanStringPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("lastName")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        emailColumn.setCellValueFactory(cellData -> {
            try {
                return JavaBeanStringPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("email")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        roleColumn.setCellValueFactory(cellData -> {
            try {
                //noinspection unchecked
                return JavaBeanObjectPropertyBuilder.create()
                        .bean(cellData.getValue())
                        .name("role")
                        .build();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        checkBoxColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(checkBoxMap.get(c.getValue())));




        sendEmailButton.setOnAction(this::handleSendAction);

        queryField.setOnKeyTyped(e -> this.setItems());

        warningMessage.setVisible(false);

        recommendedButton.setOnAction(this::sendRecommendedShows);
    }

    public ArrayList<String> getReceivers() {
        List<User> users = userService.getUsers().stream().toList();
        ArrayList<String> emails = new ArrayList<>();

        for (User user: users) {
            CheckBox checkBox = checkBoxMap.get(user);
            if (checkBox.isSelected()) emails.add(user.getEmail());
        }
        return emails;
    }
    public void handleSendAction(ActionEvent event) {
        ArrayList<String> emails = getReceivers();
        if (emails.isEmpty()) {
            warningMessage.setVisible(true);
            return;
        }
        try {
            Stage stage = new Stage();
            Pair<Parent, SendEmailController> vmLoad = viewManager.load("/fxml/emailManager/sendEmail.fxml", stage);
            Parent parent = vmLoad.getFirst();
            SendEmailController controller = vmLoad.getSecond();


            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/static/img/app-icon.png"));
            stage.setTitle("Edit user");

            controller.setEmails(emails);

            controller.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setItems() {
        ObservableList<User> receivers = userService.getUsers().filtered(user -> {
            List<String> queries = new ArrayList<>(List.of(queryField.getText().split(" ")));

            // remove empty queries
            queries.removeIf(String::isEmpty);

            if (queries.isEmpty()) {
                return true;
            }

            return queries.stream().allMatch(query -> {
                String lowerCaseQuery = query.toLowerCase();
                return user.getFirstName().toLowerCase().contains(lowerCaseQuery) ||
                        user.getLastName().toLowerCase().contains(lowerCaseQuery) ||
                        user.getEmail().toLowerCase().contains(lowerCaseQuery) ||
                        user.getRole().toString().toLowerCase().contains(lowerCaseQuery);
            });

        });
        receiversTable.setItems(receivers);
    }



    public void sendRecommendedShows(ActionEvent event) {
        List<User> assistants = userService.getUsers().filtered(user -> user.getRole().equals(Role.ASSISTANT));
        List<Show> shows = showService.getShows().filtered(Show::isRecommended);
        String showsString = "";
        for (Show show: shows) {
            showsString = showsString.concat("\n- " + show.getMovie().getTitle() + ", " + show.getRoom().getRoomName());
        }
        recommendedShowsMessage.setText(recommendedShowsMessage.getText().concat(showsString));
        for (User assistant: assistants) {
            recommendedShowsMessage.setTo(assistant.getEmail());
            mailSender.sendSimpleMessage(recommendedShowsMessage);
        }
    }
}
