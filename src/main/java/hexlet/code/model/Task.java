package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Table(name = "tasks")
@Accessors(chain = true)
public class Task {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private User assignee;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private TaskStatus taskStatus;

    @CreatedDate
    private LocalDate createdAt;

    private Long index;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    private Set<Label> labels;
}
