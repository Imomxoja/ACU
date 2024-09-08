package uz.practise.acu.domain.entity.mapper;

import lombok.*;
import org.springframework.stereotype.Component;
import uz.practise.acu.domain.entity.CardEntity;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.Role;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.UserRequest;
import uz.practise.acu.domain.response.UserResponse;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserEntityMapper {
    private String fullName;
    private String email;
    private String password;
    private List<CourseEntity> courses;
    private List<RecipeEntity> recipes;
    private List<RecipeEntity> savedRecipes;
    private Role role;
    private CardEntity card;
    private List<RankingEntity> ranking;


    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .courses(userEntity.getCourses())
                .recipes(userEntity.getRecipes())
                .savedRecipes(userEntity.getSavedRecipes())
                .ranking(userEntity.getRanking())
                .build();
    }

    public UserEntity toUserEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .description(userRequest.getDescription())
                .build();
    }
}
