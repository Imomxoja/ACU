package uz.practise.acu.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity(name = "course")
@NoArgsConstructor
@Builder
@Getter
@Setter

public class CourseEntity extends BaseEntity {
    private String fileName;
    private String name;
    private String description;
    private byte[] content;
    private boolean isNotDeleted = true;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private Double price;
    @OneToOne(mappedBy = "course")
    private RankingEntity ranking;
    @ManyToMany(mappedBy = "savedCourses")
    private List<UserEntity> users;
}
