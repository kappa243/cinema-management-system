package pl.edu.agh.cinema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
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
import pl.edu.agh.cinema.utils.ImageConverter;

import javax.transaction.Transactional;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    public CommandLineRunner demo(UserRepository userRepository, MovieRepository movieRepository, RoomRepository roomRepository, ShowRepository showRepository) {
        return args -> {
            // users
            String password = "admin";
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            User admin = new User("admin", "admin", "admin", hashed, Role.ADMINISTRATOR);
            User p1 = new User("Jan", "Kowalski", "jkowalski@example.com", hashed, Role.ASSISTANT);
            User p2 = new User("Adam", "Nowak", "anowak@example.com", hashed, Role.ASSISTANT);
            User p3 = new User("Anna", "Kowalska", "akowalska@example.com", hashed, Role.MODERATOR);


            // movies
            String movieData = """
                    Triangle of Sadness; In Ruben Östlund's wickedly funny Palme d'Or winner, social hierarchy is turned upside down, revealing the tawdry relationship between power and beauty. Celebrity model couple, Carl (Harris Dickinson) and Yaya (Charlbi Dean), are invited on a luxury cruise for the uber-rich, helmed by an unhinged boat captain (Woody Harrelson). What first appeared instagrammable ends catastrophically, leaving the survivors stranded on a desert island and fighting for survival.; 2023-01-06; 147
                    Avatar: The Way of Water; Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns to finish what was previously started, Jake must work with Neytiri and the army of the Na'vi race to protect their home.; 2022-12-16; 192
                    Glass Onion: A Knives Out Mystery; Five long-time friends are invited to the Greek island home of billionaire Miles Bron. All five know Bron from way back and owe their current wealth, fame and careers to him. The main event is a murder weekend game with Bron to be the victim. In reality, they all have reasons to kill him. Also invited is Benoit Blanc, the world's greatest detective.; 2022-12-23; 139
                    Babilon; An original epic set in 1920s Los Angeles led by Brad Pitt, Margot Robbie and Diego Calva, with an ensemble cast including Jovan Adepo, Li Jun Li and Jean Smart. A tale of outsized ambition and outrageous excess, it traces the rise and fall of multiple characters during an era of unbridled decadence and depravity in early Hollywood.; 2023-01-20; 188
                    The Banshees of Inisherin; Set on a remote island off the west coast of Ireland, THE BANSHEES OF INISHERIN follows lifelong friends Padraic (Colin Farrell) and Colm (Brendan Gleeson), who find themselves at an impasse when Colm unexpectedly puts an end to their friendship. A stunned Padraic, aided by his sister Siobhan (Kerry Condon) and troubled young islander Dominic (Barry Keoghan), endeavours to repair the relationship, refusing to take no for an answer. But Padraic's repeated efforts only strengthen his former friend's resolve and when Colm delivers a desperate ultimatum, events swiftly escalate, with shocking consequences.; 2023-01-23; 128
                    Black Adam; Reawakening after 5000 years, Black Adam becomes the world's ruthless protector: an anti-villain to take on the likes of Superman and Wonder Woman. Now in the 21st-Century, Black Adam must face off against the Justice Society of America and its heroes: Doctor Fate, Hawkman, Atom Smasher and Cyclone. The fate of the world hangs in the balance.; 2022-10-21; 125
                    Guillermo del Toro's Pinocchio; Academy Award®-winning filmmaker Guillermo del Toro reinvents Carlo Collodi's classic tale of the wooden marionette who is magically brought to life in order to mend the heart of a grieving woodcarver named Geppetto. This whimsical, stop-motion musical directed by Guillermo del Toro and Mark Gustafson follows the mischievous and disobedient adventures of Pinocchio in his pursuit of a place in the world.; 2022-12-09; 117
                    Bullet Train; Unlucky assassin Ladybug (Brad Pitt) is determined to do his job peacefully after one too many gigs has gone off the rails. Fate has other plans, however: Ladybug's latest mission puts him on a collision course with lethal adversaries from around the globe--all with connected, yet conflicting, objectives--on the world's fastest train. The end of the line is just the beginning in this non-stop thrill-ride through modern-day Japan.; 2022-08-05; 127
                    Top Gun: Maverick; Set 30 years after its predecessor, it follows Maverick's return to the United States Navy Strike Fighter Tactics Instructor program (also known as U.S. Navy-Fighter Weapons School - "TOPGUN"), where he must confront his past as he trains a group of younger pilots, among them the son of Maverick's deceased best friend Lieutenant Nick "Goose" Bradshaw, USN.; 2022-05-27; 130 
                    """;

            BufferedReader r = new BufferedReader(new StringReader(movieData));
            List<Movie> movies = new ArrayList<>();
            for (; ; ) {
                String line = r.readLine();
                if (line == null) break;
                String[] movieTab = line.split(";\\s*");

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date dateStr = formatter.parse(movieTab[2]);
                LocalDateTime localDate = dateStr.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Movie m = new Movie(movieTab[0], movieTab[1], localDate, Integer.parseInt(movieTab[3]), ImageConverter.fileToByte(new File("src/main/resources/static/img/movie-icon.png")));
                System.out.println(m);
                movies.add(m);
            }



            // rooms
            List<Room> rooms = readRooms();

            // shows
            Show show1 = new Show(Timestamp.valueOf("2023-11-12 11:00:03.123456789").toLocalDateTime(), Timestamp.valueOf("2023-11-12 13:02:03.123456789").toLocalDateTime(), Timestamp.valueOf("2018-11-12 01:02:03.123456789").toLocalDateTime(), 12, 38);
            Show show2 = new Show(Timestamp.valueOf("2023-11-12 15:00:03.123456789").toLocalDateTime(), Timestamp.valueOf("2023-11-12 17:02:03.123456789").toLocalDateTime(), Timestamp.valueOf("2018-11-12 01:02:03.123456789").toLocalDateTime(), 40, 24);
            Show show3 = new Show(Timestamp.valueOf("2023-01-12 15:00:03.123456789").toLocalDateTime(), Timestamp.valueOf("2023-01-12 17:25:03.123456789").toLocalDateTime(), Timestamp.valueOf("2022-12-10 11:20:00.123456789").toLocalDateTime(), 32, 10);

            show1.setMovie(movies.get(0));
            show1.setRoom(rooms.get(0));

            show2.setMovie(movies.get(1));
            show2.setRoom(rooms.get(1));

            show3.setMovie(movies.get(2));
            show3.setRoom(rooms.get(2));


            userRepository.saveAll(List.of(admin, p1, p2, p3));
            roomRepository.saveAll(rooms);
            movieRepository.saveAll(movies);
            showRepository.saveAll(List.of(show1, show2, show3));
        };
    }


    List<Room> readRooms() {
        List<Room> rooms = new ArrayList<>();
        try {
            JsonObject data = JsonParser.parseReader(new JsonReader(new BufferedReader(new FileReader("src/main/resources/rooms.json")))).getAsJsonObject();
            JsonArray jsonRooms = data.getAsJsonArray("rooms");

            for (JsonElement jsonRoom : jsonRooms) {
                JsonObject jo = jsonRoom.getAsJsonObject();
                Room room = new Room(jo.get("name").getAsString(), jo.get("capacity").getAsInt());
                rooms.add(room);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rooms;
    }
}
