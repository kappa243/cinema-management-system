package pl.edu.agh.cinema.model.show;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    private ObservableList<Show> shows;

    public void fetchShows() {
        this.shows = FXCollections.observableArrayList(showRepository.findAll());
    }

    public ObservableList<Show> getShows() {
        if (shows == null) {
            fetchShows();
        }
        return shows;
    }

    public void addShow(Show show) {
        showRepository.save(show);
        shows.add(show); // add to observable list, but we are not fetching from database again
    }

    public void deleteShow(Show show) {
        showRepository.delete(show);
        shows.remove(show); // remove from observable list, but we are not fetching from database again
    }

    public void updateShow(Show show) {
        showRepository.save(show);
    }
}
