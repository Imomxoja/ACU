package uz.practise.acu.domain.entity.mapper;

import lombok.*;
import org.springframework.stereotype.Component;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.CourseRequest;
import uz.practise.acu.domain.response.CourseResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class CourseEntityMapper {
    private String name;
    private String description;
    private UserEntity user;
    private Double price;
    private RankingEntity ranking;

    public CourseResponse toResponse(CourseEntity courseEntity) {
        return CourseResponse.builder()
                .id(courseEntity.getId())
                .description(courseEntity.getDescription())
                .price(courseEntity.getPrice())
                .content(courseEntity.getContent())
                .fileName(courseEntity.getFileName())
                .ranking(courseEntity.getRanking())
                .user(courseEntity.getUser())
                .build();
    }

    @SneakyThrows
    public CourseEntity toEntity(CourseRequest courseRequest) {
        return CourseEntity.builder()
                .price(courseRequest.getPrice())
                .description(courseRequest.getDescription())
                .name(courseRequest.getName())
                .fileName(courseRequest.getFileName())
                .build();
    }
}
