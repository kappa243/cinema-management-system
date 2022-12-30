package pl.edu.agh.cinema.model.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

//    @Query(value="select CASE WHEN (select count(*) from movie) > 0 THEN (select MAX(id) from movie)+1 ELSE  1 END;", nativeQuery = true)
//    Long getFreeId();
}
