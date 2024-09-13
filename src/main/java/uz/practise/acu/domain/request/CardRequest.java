package uz.practise.acu.domain.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRequest {
    private Double balance;
    private String cardNumber;
}
