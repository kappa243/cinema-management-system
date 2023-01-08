package pl.edu.agh.cinema.model.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {
    //zwroc seanse dla danego filmu
    @Query(value="SELECT * from Show WHERE movie_id=?1", nativeQuery = true)
    List<Show> getShowsForMovie(Long id);

    //wez showy dla danego dnia i sali
    @Query(value="SELECT * from Show WHERE room_id=?4 AND DAY(start_time)=?1 AND MONTH(start_time)=?2 AND YEAR(start_time)=?3", nativeQuery = true)
    List<Show> getShowsForDateAndRoom(int day, int month, int year, Long id);
}
