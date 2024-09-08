package uz.practise.acu.domain.response;

import lombok.*;
import uz.practise.acu.domain.entity.CardEntity;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.Role;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private double averageRating;
    private List<CourseEntity> courses;
    private List<RecipeEntity> recipes;
    private List<RecipeEntity> savedRecipes;
    private Role role;
    private CardEntity card;
    private List<RankingEntity> ranking;
}
