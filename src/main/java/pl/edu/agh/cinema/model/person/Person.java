package pl.edu.agh.cinema.model.person;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


@Entity
public class Person {

    @Transient
    private final PropertyChangeSupport pcs;

    @Id
    @GeneratedValue
    @Getter
    private int id;


    @Getter
    private String firstName;

    public void setFirstName(String firstName) {
        String oldFirstName = this.firstName;
        this.firstName = firstName;
        pcs.firePropertyChange("firstName", oldFirstName, firstName);
    }

    @Getter
    private String lastName;

    public void setLastName(String lastName) {
        String oldLastName = this.lastName;
        this.lastName = lastName;
        pcs.firePropertyChange("lastName", oldLastName, lastName);
    }

    @Getter
    private String email;

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        pcs.firePropertyChange("email", oldEmail, email);
    }


    public Person() {
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public Person(String firstName, String lastName, String email) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
