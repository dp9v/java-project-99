package hexlet.code.dto;

public record TaskFilterRequest(
        String titleCont,
        Long assigneeId,
        String status,
        Long labelId
) {
}
