package pl.edu.agh.cinema.auth.permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.springframework.stereotype.Service;
import pl.edu.agh.cinema.model.user.Role;
import pl.edu.agh.cinema.model.user.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Optional;

@Service
public class PermissionService {

    private final String PATH = "src/main/resources/permissions.json";

    private final HashMap<Role, PermissionRole> permissionRoles = new HashMap<>();

    public PermissionService() {
        try {
            loadPermissionsFiles();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasPermission(User user, String permission){
        return permissionRoles.get(user.getRole()).hasPermission(permission);
    }

    private void loadPermissionsFiles() throws FileNotFoundException {
        JsonObject data = JsonParser.parseReader(new JsonReader(new BufferedReader(new FileReader(PATH)))).getAsJsonObject();
        data.entrySet().forEach(entry -> {
            Role role = Role.valueOf(entry.getKey());
            JsonObject values = entry.getValue().getAsJsonObject();

            JsonArray base = values.get("base").getAsJsonArray();
            JsonArray permissions = values.get("permissions").getAsJsonArray();

            PermissionRole permissionRole = new PermissionRole();
            base.forEach(baseRole -> {
                Optional<PermissionRole> basePermissionRole = Optional.ofNullable(permissionRoles.get(Role.valueOf(baseRole.getAsString())));
                basePermissionRole.ifPresent(permissionRole::addBaseRole);
            });

            permissions.forEach(permission -> permissionRole.addPermission(permission.getAsString()));

            permissionRoles.put(role, permissionRole);
        });
    }
}
