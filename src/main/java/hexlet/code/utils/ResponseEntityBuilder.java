package hexlet.code.utils;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityBuilder {

    public static <T> ResponseEntity<List<T>> build(List<T> items) {
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(items.size()))
            .body(items);
    }
}
