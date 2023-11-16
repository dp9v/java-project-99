package hexlet.code.app.dtos;

public record TaskFilterRequest(
        String titleCont,
        Long assigneeId,
        String status,
        Long labelId
) {
}
