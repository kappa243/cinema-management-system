package pl.edu.agh.cinema.auth.permissions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PermissionRole {

    private final List<PermissionRole> base;
    private final HashSet<String> permissions;

    public PermissionRole() {
        this.base = new ArrayList<>();
        this.permissions = new HashSet<>();
    }

    public void addBaseRole(PermissionRole permissionRole) {
        base.add(permissionRole);
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public boolean hasPermission(String permission) {
        if (permissions.contains(permission)) {
            return true;
        }
        for (PermissionRole permissionRole : base) {
            if (permissionRole.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
}
