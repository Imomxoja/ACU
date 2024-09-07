package uz.practise.acu.domain.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import uz.practise.acu.domain.entity.Category;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecipeRequest {
    private String name;
    private String description;
    private Category category;
    private Boolean isPaid;
    private MultipartFile image;
}
