package pl.edu.agh.cinema.model.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private ObservableList<User> users;

    public void fetchUsers() {
        this.users = FXCollections.observableArrayList(userRepository.findAll());
    }

    public ObservableList<User> getUsers() {
        if (users == null) {
            fetchUsers();
        }
        return users;
    }

    public void addUser(User user) {
        userRepository.save(user);
        users.add(user); // add to observable list, but we are not fetching from database again
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
        users.remove(user); // remove from observable list, but we are not fetching from database again
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
