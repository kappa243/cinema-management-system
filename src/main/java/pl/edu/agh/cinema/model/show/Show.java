package pl.edu.agh.cinema.model.show;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.room.Room;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Show {

    @Transient
    private final PropertyChangeSupport pcs;

    @Id
    @GeneratedValue
    @Getter
    Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @Getter
    Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @Getter
    Room room;

    @Getter
    LocalDateTime startTime;

    @Getter
    LocalDateTime endTime;

    @Getter
    LocalDateTime sellTicketsFrom;

    @Getter
    double ticketPrice;

    @Getter
    int soldTickets;

    @Getter
    @Setter
    private boolean recommended = false;

    public void setMovie(Movie movie) {
        Movie oldMovie = this.movie;
        if(oldMovie != null) {
            oldMovie.removeShow(this);
        }

        this.movie = movie;
        movie.addShow(this);

        pcs.firePropertyChange("movie", oldMovie, movie);
    }

    public void setRoom(Room room) {
        Room oldRoom = this.room;

        this.room = room;
        pcs.firePropertyChange("room", oldRoom, room);
    }

    public void setStartTime(LocalDateTime startTime) {
        LocalDateTime oldStartTime = this.startTime;
        this.startTime = startTime;
        pcs.firePropertyChange("startTime", oldStartTime, startTime);
    }

    public void setEndTime(LocalDateTime endTime) {
        LocalDateTime oldEndTime = this.endTime;
        this.endTime = endTime;
        pcs.firePropertyChange("endTime", oldEndTime, endTime);
    }

    public void setSellTicketsFrom(LocalDateTime sellTicketsFrom) {
        LocalDateTime oldSellTicketsFrom = this.sellTicketsFrom;
        this.sellTicketsFrom = sellTicketsFrom;
        pcs.firePropertyChange("sellTicketsFrom", oldSellTicketsFrom, sellTicketsFrom);
    }

    public void setTicketPrice(double ticketPrice) {
        double oldTicketPrice = this.ticketPrice;
        this.ticketPrice = ticketPrice;
        pcs.firePropertyChange("ticketPrice", oldTicketPrice, ticketPrice);
    }

    public void setSoldTickets(int soldTickets) {
        int oldSoldTickets = this.soldTickets;
        this.soldTickets = soldTickets;
        pcs.firePropertyChange("soldTickets", oldSoldTickets, soldTickets);
    }


    public Show() {
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public Show(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime sellTicketsFrom, double ticketPrice, int soldTickets) {
        this();
        this.startTime = startTime;
        this.endTime = endTime;
        this.sellTicketsFrom = sellTicketsFrom;
        this.ticketPrice = ticketPrice;
        this.soldTickets = soldTickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return Objects.equals(id, show.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}