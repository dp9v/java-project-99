package hexlet.code.app.repositories;

import hexlet.code.app.models.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelsRepository extends JpaRepository<Label, Long> {
}
