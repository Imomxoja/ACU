package uz.practise.acu.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "recipe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecipeEntity extends BaseEntity {
    private String name;
    private String description;
    private Category category;
    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    private RankingEntity ranking;
    private Double price;
    private Boolean isNotDeleted = Boolean.TRUE;
    @Lob
    private byte[] image;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToMany(mappedBy = "savedRecipes")
    private List<UserEntity> users;
}
