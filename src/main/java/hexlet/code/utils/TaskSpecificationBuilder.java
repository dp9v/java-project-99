package hexlet.code.utils;

import hexlet.code.dto.TaskFilterRequest;
import hexlet.code.model.Task;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecificationBuilder {
    public Specification<Task> build(TaskFilterRequest filter) {
        return withNameCont(filter.titleCont())
                .and(withAssigneeId(filter.assigneeId()))
                .and(withStatusSlug(filter.status()))
                .and(withLabelId(filter.labelId()));
    }

    private Specification<Task> withNameCont(String name) {
        return (root, query, cb) -> name == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + name + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("name").get("id"), assigneeId);
    }

    private Specification<Task> withStatusSlug(String slug) {
        return (root, query, cb) -> slug == null
                ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), slug);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null
                ? cb.conjunction()
                : cb.equal(root.join("labels", JoinType.INNER).get("id"), labelId);
    }
}
