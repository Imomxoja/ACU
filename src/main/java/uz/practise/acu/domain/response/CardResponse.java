package uz.practise.acu.domain.response;

import lombok.*;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {
    private UUID id;
    private Double balance;
    private String cardNumber;
    private UserEntity user;
}
