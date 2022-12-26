package pl.edu.agh.cinema.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ApplicationCloseEvent;
import pl.edu.agh.cinema.StageManager;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.model.user.User;
import pl.edu.agh.cinema.model.user.UserService;
import pl.edu.agh.cinema.model.user.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.IOException;

@Component
@Scope("prototype")
public class ApplicationController {

    @Autowired
    ApplicationEventPublisher publisher;
    StageManager stageManager;
    ViewManager viewManager;

    UserService userService;

    @FXML
    private Button exitButton;

    @FXML
    private Button addNewUserButton;

    @FXML
    private Button editUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    @Enumerated(EnumType.STRING)
    private TableColumn<User, Role> roleColumn;


    public ApplicationController(ApplicationEventPublisher publisher,
                                 StageManager stageManager,
                                 ViewManager viewManager,
                                 UserService userService) {
        this.publisher = publisher;
        this.viewManager = viewManager;
        this.stageManager = stageManager;

        this.userService = userService;
    }

    @FXML
    public void initialize() {
        usersTable.setItems(userService.getUsers());

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

        exitButton.setOnAction(event -> publisher.publishEvent(new ApplicationCloseEvent(this)));

        addNewUserButton.setOnAction(this::handleAddAction);

        editUserButton.disableProperty().bind(
                Bindings.size(usersTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
        );
        editUserButton.setOnAction(this::handleEditAction);

        deleteUserButton.setOnAction(this::handleDeleteAction);
    }


    private void handleAddAction(ActionEvent event) {
        try {
            Stage stage = new Stage();

            Pair<Parent, EditUserController> vmLoad = viewManager.load("/fxml/editUser.fxml", stage);
            Parent parent = vmLoad.getFirst();
            EditUserController controller = vmLoad.getSecond();

            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(stageManager.getPrimaryStage());
            stage.setResizable(false);
            stage.setTitle("Add new user");

            User user = new User("", "", "", Role.ASSISTANT);
            controller.setData(user);
            controller.setStage(stage);

            // TODO - temporary solution
            controller.renameButton("Add user");

            stage.showAndWait();

            if (controller.isConfirmed()) {
                userService.addUser(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Pair<Parent, EditUserController> vmLoad = viewManager.load("/fxml/editUser.fxml", stage);
            Parent parent = vmLoad.getFirst();
            EditUserController controller = vmLoad.getSecond();


            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(stageManager.getPrimaryStage());
            stage.setResizable(false);
            stage.setTitle("Edit user");

            User user = usersTable.getSelectionModel().getSelectedItem();
            controller.setData(user);

            stage.showAndWait();

            if (controller.isConfirmed()) {
                userService.updateUser(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAction(ActionEvent event) {
        User user = usersTable.getSelectionModel().getSelectedItem();
        userService.deleteUser(user);
    }
}
