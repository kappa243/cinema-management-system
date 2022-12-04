package pl.edu.agh.cinema;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import pl.edu.agh.cinema.model.person.Person;
import pl.edu.agh.cinema.model.person.PersonRepository;

import java.util.List;

@SpringBootApplication
public class CinemaApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(CinemaApplication.class, args);
    }


    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(CinemaApplication.class)
                .sources(CinemaApplication.class)
                .initializers(initializers())
                .run(getParameters().getRaw().toArray(new String[0]));

    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new ApplicationReadyEvent(this, stage));
    }

    @Override
    public void stop() {
        applicationContext.close(); // close Spring
        Platform.exit(); // close JavaFX
    }


    ApplicationContextInitializer<GenericApplicationContext> initializers() {
        return applicationContext -> {
            applicationContext.registerBean(Application.class, () -> CinemaApplication.this);
            applicationContext.registerBean(Parameters.class, this::getParameters);
        };
    }

    @Bean
    public CommandLineRunner demo(PersonRepository personRepository) {
        return args -> {
            Person p1 = new Person("Jan", "Kowalski", "jkowalski@example.com");
            Person p2 = new Person("Adam", "Nowak", "anowak@example.com");
            Person p3 = new Person("Anna", "Kowalska", "akowalska@example.com");

            personRepository.saveAll(List.of(p1, p2, p3));


        };
    }
}
