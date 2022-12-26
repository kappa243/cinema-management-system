package pl.edu.agh.cinema.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.edu.agh.cinema.auth.permissions.PermissionService;
import pl.edu.agh.cinema.model.user.Role;
import pl.edu.agh.cinema.model.user.User;
import pl.edu.agh.cinema.model.user.UserService;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PermissionService permissionService;
    private final UserContext userContext = new UserContext();

    public AuthenticationService(UserService userService, PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    public void authenticate(String email, String password) throws AuthenticationException {
        var user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new AuthenticationException("User not found") {
            };
        }
        if (!BCrypt.checkpw(password, user.get().getPassword())) {
            throw new AuthenticationException("Wrong password") {
            };
        }

        userContext.setUser(user.get());
    }

    public boolean isAuthenticated() {
        return userContext.isUserLoggedIn();
    }


    public void registerUser(String firstName, String lastName, String email, String password, Role role) throws IllegalArgumentException {
        if (isEmailTaken(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(firstName, lastName, email, hashedPassword, role);
        userService.addUser(user);
    }

    public void login(String email, String password) throws AuthenticationException {
        authenticate(email, password);
    }

    public void logout() {
        userContext.logout();
    }

    private boolean isEmailTaken(String email) {
        return userService.findUserByEmail(email).isPresent();
    }

    public boolean isAuthorized(String permission) {
        return userContext.getUser().map(user -> permissionService.hasPermission(user, permission)).orElse(false);
    }
}
