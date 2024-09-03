package uz.practise.acu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.practise.acu.domain.entity.user.UserEntity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity(name = "course")
@NoArgsConstructor
@Data
public class CourseEntity extends BaseEntity {
    private String name;
    private String description;
    @Lob
    private byte[] video;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @OneToOne(mappedBy = "course")
    private RankingEntity ranking;
}
