package uz.practise.acu.domain.entity.mapper;

import lombok.*;
import org.springframework.stereotype.Component;
import uz.practise.acu.domain.entity.CardEntity;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.CardRequest;
import uz.practise.acu.domain.response.CardResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CardEntityMapper {
    private Double balance;
    private String cardNumber;
    private UserEntity user;

    public CardEntity toCardEntity(CardRequest card) {
        return CardEntity.builder()
                .balance(card.getBalance())
                .cardNumber(card.getCardNumber())
                .build();
    }

    public CardRequest toCardRequest(CardEntity card) {
        return CardRequest.builder()
                .balance(card.getBalance())
                .cardNumber(card.getCardNumber())
                .build();
    }

    public CardResponse toCardResponse(CardEntity card) {
        return CardResponse.builder()
                .balance(card.getBalance())
                .cardNumber(card.getCardNumber())
                .user(card.getUser())
                .build();
    }
}
