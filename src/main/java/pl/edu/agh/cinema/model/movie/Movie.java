package pl.edu.agh.cinema.model.movie;

import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.cinema.model.show.Show;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Movie {

    @Transient
    private final PropertyChangeSupport pcs;

    @Id
    @GeneratedValue
    @Getter
    private int id;

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
    LocalDateTime releaseDate;

    public void setReleaseDate(LocalDateTime releaseDate) {
        LocalDateTime oldReleaseDate = this.releaseDate;
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
    @Lob
    private byte[] cover;

    public void setCover(byte[] cover) {
        byte[] oldCover = this.cover;
        this.cover = cover;
        pcs.firePropertyChange("cover", oldCover, cover);
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Show> shows = new HashSet<>();

    public Set<Show> getShows(){
        return shows;
    }

    public void addShow(Show show) {
        this.shows.add(show);

        pcs.firePropertyChange("shows", null, shows);
    }

    public void removeShow(Show show) {
        this.shows.remove(show);

        pcs.firePropertyChange("shows", null, shows);
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

    public Movie(String title, String description, LocalDateTime releaseDate, int duration, byte[] cover) {
        this();

        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.cover = cover;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
