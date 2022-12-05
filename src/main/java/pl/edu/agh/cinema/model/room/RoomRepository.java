package pl.edu.agh.cinema.model.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cinema.model.person.Person;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
