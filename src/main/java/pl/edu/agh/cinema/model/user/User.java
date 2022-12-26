package pl.edu.agh.cinema.model.user;

import lombok.Getter;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


@Entity(name = "person")
public class User {

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

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    public void setRole(Role role) {
        Role oldRole = this.role;
        this.role = role;
        pcs.firePropertyChange("role", oldRole, role);
    }

    public User() {
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public User(String firstName, String lastName, String email, Role role) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
