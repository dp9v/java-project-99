package hexlet.code.app.utils;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.function.Function;

public class ResponseEntityBuilder {
    public static <T, R> ResponseEntity<List<R>> build(List<T> items, Function<T, R> mapper) {
        var result = items.stream()
                .map(mapper)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }
}
