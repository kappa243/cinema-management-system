package pl.edu.agh.cinema.model.sales;

import lombok.Data;
import lombok.Getter;
import pl.edu.agh.cinema.model.show.Show;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="sales")
public class Sales {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    @JoinColumn(name = "show_id")
    Show show;

    LocalDateTime startTime;
    LocalDateTime endTime;
    int soldTickets;
}
