package uz.practise.acu.domain.response;

import lombok.*;
import uz.practise.acu.domain.entity.Category;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecipeResponse {
    private UUID id;
    private String name;
    private String description;
    private Category category;
    private RankingEntity ranking;
    private Boolean isPaid;
    private byte[] image;
    private UserEntity user;
}
