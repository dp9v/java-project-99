package hexlet.code.app.repositories;

import hexlet.code.app.models.Label;
import hexlet.code.app.models.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelsRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByName(String name);
}
