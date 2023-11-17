package hexlet.code.repositories;

import hexlet.code.models.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelsRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByName(String name);
}
