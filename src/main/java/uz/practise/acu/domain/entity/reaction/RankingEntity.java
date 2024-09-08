package uz.practise.acu.domain.entity.reaction;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import uz.practise.acu.domain.entity.BaseEntity;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity(name = "ranking")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RankingEntity extends BaseEntity {
    private List<String> comments;
    private int likes;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;
    @OneToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;
    @OneToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
