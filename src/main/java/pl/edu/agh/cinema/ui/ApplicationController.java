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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import pl.edu.agh.cinema.ApplicationCloseEvent;
import pl.edu.agh.cinema.StageManager;
import pl.edu.agh.cinema.ViewManager;
import pl.edu.agh.cinema.model.person.Person;
import pl.edu.agh.cinema.model.person.PersonService;
import pl.edu.agh.cinema.model.person.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.IOException;

@Component
@Scope("prototype")
public class ApplicationController {

    ApplicationEventPublisher publisher;
    StageManager stageManager;
    ViewManager viewManager;

    PersonService personService;

    @FXML
    private Button exitButton;

    @FXML
    private Button addNewUserButton;

    @FXML
    private Button editUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private TableView<Person> usersTable;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;
    @FXML
    @Enumerated(EnumType.STRING)
    private TableColumn<Person, String> roleColumn;


    public ApplicationController(ApplicationEventPublisher publisher,
                                 StageManager stageManager,
                                 ViewManager viewManager,
                                 PersonService personService) {
        this.publisher = publisher;
        this.viewManager = viewManager;
        this.stageManager = stageManager;

        this.personService = personService;
    }

    @FXML
    public void initialize() {
        usersTable.setItems(personService.getPersons());

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

            Person person = new Person("", "", "", Role.ASSISTANT);
            controller.setData(person);
            controller.setStage(stage);

            // TODO - temporary solution
            controller.renameButton("Add user");

            stage.showAndWait();

            if (controller.isConfirmed()) {
                personService.addPerson(person);
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

            Person person = usersTable.getSelectionModel().getSelectedItem();
            controller.setData(person);

            stage.showAndWait();

            if (controller.isConfirmed()) {
                personService.updatePerson(person);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAction(ActionEvent event) {
        Person person = usersTable.getSelectionModel().getSelectedItem();
        personService.deletePerson(person);
    }
}
