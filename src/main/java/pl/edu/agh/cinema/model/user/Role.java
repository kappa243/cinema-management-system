package pl.edu.agh.cinema.model.user;

public enum Role {
    ADMINISTRATOR,
    MODERATOR,
    ASSISTANT;

    @Override
    public String toString() {
        return switch (this) {
            case ASSISTANT -> "Assistant";
            case MODERATOR -> "Moderator";
            case ADMINISTRATOR -> "Administrator";
        };
    }
}
