package pl.edu.agh.cinema.auth;

import pl.edu.agh.cinema.model.user.User;

import java.util.Optional;


public class UserContext {
    private User user = null;

    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUserLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }
}
