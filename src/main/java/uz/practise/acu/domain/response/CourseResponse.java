package uz.practise.acu.domain.response;

import lombok.*;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private UUID id;
    private String fileName;
    private byte[] content;
    private String description;
    private UserEntity user;
    private Double price;
    private RankingEntity ranking;
}
