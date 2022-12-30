package pl.edu.agh.cinema.model.movie;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.cinema.model.show.Show;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.util.Set;

@Entity
public class Movie {

    @Transient
    private final PropertyChangeSupport pcs;

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    String title;

    public void setTitle(String title) {
        String oldTitle = this.title;
        this.title = title;
        pcs.firePropertyChange("title", oldTitle, title);
    }

    @Getter
    @Column(columnDefinition = "TEXT")
    String description;

    public void setDescription(String description) {
        String oldDescription = this.description;
        this.description = description;
        pcs.firePropertyChange("description", oldDescription, description);
    }

    @Getter
    Date releaseDate;

    public void setReleaseDate(Date releaseDate) {
        Date oldReleaseDate = this.releaseDate;
        this.releaseDate = releaseDate;
        pcs.firePropertyChange("releaseDate", oldReleaseDate, releaseDate);
    }

    @Getter
    int duration;

    public void setDuration(int duration) {
        int oldDuration = this.duration;
        this.duration = duration;
        pcs.firePropertyChange("duration", oldDuration, duration);
    }

    @Getter
    @OneToMany(mappedBy = "movie")
    Set<Show> shows;

    public void setShows(Set<Show> shows) {
        Set<Show> oldShows = this.shows;
        this.shows = shows;
        pcs.firePropertyChange("shows", oldShows, shows);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public Movie() {
        pcs = new PropertyChangeSupport(this);
    }

    public Movie(String title, String description, Date releaseDate, int duration) {
        this();
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }


}
