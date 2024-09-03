package uz.practise.acu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.practise.acu.domain.entity.user.UserEntity;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "recipe")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeEntity extends BaseEntity {
    private String name;
    private String description;
    private Category category;
    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    private RankingEntity ranking;
    private Boolean isPaid;
    @Lob
    private byte[] image;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
