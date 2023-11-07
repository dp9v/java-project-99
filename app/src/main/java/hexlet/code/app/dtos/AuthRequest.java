package hexlet.code.app.dtos;

public record AuthRequest(
    String username,
    String password
) {
}
