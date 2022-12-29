package pl.edu.agh.cinema.model.show;

import lombok.Getter;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.room.Room;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Timestamp;

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
    Timestamp startTime;

    @Getter
    Timestamp endTime;

    @Getter
    Timestamp sellTicketsFrom;

    @Getter
    double ticketPrice;

    @Getter
    int soldTickets;

    public void setMovie(Movie movie) {
        Movie oldMovie = this.movie;
        this.movie = movie;
        pcs.firePropertyChange("movie", oldMovie, movie);
    }

    public void setRoom(Room room) {
        Room oldRoom = this.room;
        this.room = room;
        pcs.firePropertyChange("room", oldRoom, movie);
    }

    public void setStartTime(Timestamp startTime) {
        Timestamp oldStartTime = this.startTime;
        this.startTime = startTime;
        pcs.firePropertyChange("startTime", oldStartTime, startTime);
    }

    public void setEndTime(Timestamp endTime) {
        Timestamp oldEndTime = this.endTime;
        this.endTime = endTime;
        pcs.firePropertyChange("endTime", oldEndTime, endTime);
    }

    public void setSellTicketsFrom(Timestamp sellTicketsFrom) {
        Timestamp oldSellTicketsFrom = this.sellTicketsFrom;
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

    public Show(Timestamp startTime, Timestamp endTime, Timestamp sellTicketsFrom, double ticketPrice, int soldTickets){
        this();
        this.startTime=startTime;
        this.endTime=endTime;
        this.sellTicketsFrom=sellTicketsFrom;
        this.ticketPrice=ticketPrice;
        this.soldTickets=soldTickets;

    }
}