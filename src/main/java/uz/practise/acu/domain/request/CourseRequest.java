package uz.practise.acu.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    private String name;
    private String description;
    private String fileName;
    private Double price;
}
