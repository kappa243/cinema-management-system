package pl.edu.agh.cinema.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;


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
    @Column(unique = true, nullable = false)
    private String email;

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        pcs.firePropertyChange("email", oldEmail, email);
    }

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void setRole(Role role) {
        Role oldRole = this.role;
        this.role = role;
        pcs.firePropertyChange("role", oldRole, role);
    }

    @Getter
    @Setter // dont allow property binding (silent change)
    @Column(nullable = false)
    private String password;

    public User() {
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public User(String firstName, String lastName, String email, String password, Role role) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
