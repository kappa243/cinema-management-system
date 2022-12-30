package pl.edu.agh.cinema.model.movie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;


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

    public void updateMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
//        movies.remove(movie); // remove from observable list, but we are not fetching from database again
    }
}
