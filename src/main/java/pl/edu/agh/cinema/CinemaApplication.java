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
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.movie.MovieRepository;
import pl.edu.agh.cinema.model.room.Room;
import pl.edu.agh.cinema.model.room.RoomRepository;
import pl.edu.agh.cinema.model.show.Show;
import pl.edu.agh.cinema.model.show.ShowRepository;
import pl.edu.agh.cinema.model.user.Role;
import pl.edu.agh.cinema.model.user.User;
import pl.edu.agh.cinema.model.user.UserRepository;

import java.sql.Date;
import java.sql.Timestamp;
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
    public CommandLineRunner demo(UserRepository userRepository, ShowRepository showRepository,
                                  MovieRepository movieRepository, RoomRepository roomRepository) {
        return args -> {
            String password = "admin";
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            User admin = new User("admin", "admin", "admin", hashed, Role.ADMINISTRATOR);
            User p1 = new User("Jan", "Kowalski", "jkowalski@example.com", hashed, Role.ASSISTANT);
            User p2 = new User("Adam", "Nowak", "anowak@example.com", hashed, Role.ASSISTANT);
            User p3 = new User("Anna", "Kowalska", "akowalska@example.com", hashed, Role.MODERATOR);

            userRepository.saveAll(List.of(admin, p1, p2, p3));

            Movie m1 = new Movie("Once Upon a Time In Wild West", "nice", new Date(1968, 6, 15));
            movieRepository.save(m1);

            Room r1 = new Room("room1", 60);

            roomRepository.save(r1);

            Show s1 = new Show(new Timestamp(2023, 1, 1, 0, 0, 0, 0),
                    new Timestamp(2023, 1, 1, 0, 0, 0, 0),
                    new Timestamp(2023, 1, 1, 0, 0, 0, 0),
                    12.35, 12);
            s1.setRoom(r1);
            s1.setMovie(m1);
            showRepository.save(s1);
        };
    }
}
