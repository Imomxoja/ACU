package uz.practise.acu.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "ranking")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RankingEntity extends BaseEntity {
    private List<String> comments;
    private Long likes;
    @OneToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;
    @OneToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
}
