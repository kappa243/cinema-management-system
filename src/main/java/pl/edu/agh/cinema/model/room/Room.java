package pl.edu.agh.cinema.model.room;

import lombok.Getter;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

@Entity
public class Room {

    @Transient
    private final PropertyChangeSupport pcs;

    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Getter
    private String roomName;

    public void setRoomName(String roomName) {
        String oldRoomName = this.roomName;
        this.roomName = roomName;
        pcs.firePropertyChange("roomName", oldRoomName, roomName);
    }


    @Getter
    private int seatsNumber;

    public void setSeatsNumber(int seatsNumber) {
        int oldSeatsNumber = this.seatsNumber;
        this.seatsNumber = seatsNumber;
        pcs.firePropertyChange("seatsNumber", oldSeatsNumber, seatsNumber);
    }

    public Room(){
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public Room(String roomName, int seatsNumber) {
        this();

        this.roomName=roomName;
        this.seatsNumber=seatsNumber;
    }

    @Override
    public String toString() {
        return getRoomName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
