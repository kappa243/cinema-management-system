package pl.edu.agh.cinema.model.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.agh.cinema.model.movie.Movie;
import pl.edu.agh.cinema.model.show.Show;

import java.util.List;


@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {

    @Query(value="select sum(sales.sold_tickets) from sales " +
            "inner join show on sales.show_id=show.id " +
            "where show.movie_id=?4 and " +
            "year(sales.start_time)=?3 and month(sales.start_time)=?2 and day(sales.start_time)=?1", nativeQuery = true)
    int getSoldTicketsForMovieAndDay(int day, int month, int year, long movieId);

    @Query(value="select sum(sales.sold_tickets*show.ticket_price) from sales " +
            "inner join show on sales.show_id=show.id " +
            "where show.movie_id=?4 and " +
            "year(sales.start_time)=?3 and month(sales.start_time)=?2 and day(sales.start_time)=?1", nativeQuery = true)
    int getSoldTicketsValueForMovieAndDay(int day, int month, int year, long movieId);

    @Query(value="select sum(sales.sold_tickets) from sales " +
            "where year(sales.start_time)=?3 and month(sales.start_time)=?2 and day(sales.start_time)=?1", nativeQuery = true)
    int getSoldTicketsForDay(int day, int month, int year);

    @Query(value="select sum(sales.sold_tickets*show.ticket_price) from sales " +
            "inner join show on sales.show_id=show.id " +
            "where year(sales.start_time)=?3 and month(sales.start_time)=?2 and day(sales.start_time)=?1", nativeQuery = true)
    int getSoldTicketsValueForDay(int day, int month, int year);

}
