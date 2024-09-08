package uz.practise.acu.domain.entity.mapper;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uz.practise.acu.domain.entity.Category;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.RecipeRequest;
import uz.practise.acu.domain.response.RecipeResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class RecipeEntityMapper {
    private String name;
    private String description;
    private Category category;
    private RankingEntity ranking;
    private Boolean isPaid;
    private MultipartFile image;
    private UserEntity user;

    @SneakyThrows
    public RecipeEntity toEntity(RecipeRequest recipeRequest, MultipartFile multipartFile) {
        return RecipeEntity.builder()
                .category(recipeRequest.getCategory())
                .image(multipartFile.getBytes())
                .description(recipeRequest.getDescription())
                .name(recipeRequest.getName())
                .build();
    }

    @SneakyThrows
    public RecipeResponse toResponse(RecipeEntity entity) {
        return RecipeResponse.builder()
                .id(entity.getId())
                .ranking(entity.getRanking())
                .user(entity.getUser())
                .category(entity.getCategory())
                .image(entity.getImage())
                .description(entity.getDescription())
                .name(entity.getName())
                .build();
    }
}
