package pl.edu.agh.cinema.model.movie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.cinema.model.show.Show;

import java.util.List;
import java.util.Set;


@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private ObservableList<Movie> movies;

    public void fetchMovies() {
        this.movies = FXCollections.observableArrayList(movieRepository.findAll());
    }

    public ObservableList<Movie> getMovies() {
        if (movies == null) {
            fetchMovies();
        }
        return movies;
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
        movies.add(movie); // add to observable list, but we are not fetching from database again
    }

    @Transactional
    public boolean deleteMovie(Movie movie) {
        
        if (movieRepository.getReferenceById(movie.getId()).getShows().isEmpty()) {
            movieRepository.delete(movie);
            movies.remove(movie);  // remove from observable list, but we are not fetching from database again
            return true;
        }
        return false;
    }

    public void updateMovie(Movie movie) {
        movieRepository.save(movie);
    }
}
