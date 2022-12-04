package pl.edu.agh.cinema.model.room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    private ObservableList<Room> rooms;

    public void fetchRooms() {
        this.rooms = FXCollections.observableArrayList(roomRepository.findAll());
    }

    public ObservableList<Room> getRooms() {
        if (rooms == null) {
            fetchRooms();
        }
        return rooms;
    }

    public void addRoom(Room room) {
        roomRepository.save(room);
        rooms.add(room); // add to observable list, but we are not fetching from database again
    }

    public void updateRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoom(Room room) {
        roomRepository.delete(room);
        rooms.remove(room); // remove from observable list, but we are not fetching from database again
    }
}