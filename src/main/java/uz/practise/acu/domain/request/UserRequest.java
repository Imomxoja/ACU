package uz.practise.acu.domain.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    private String fullName;
    private String password;
    private String description;
    private String email;
}
