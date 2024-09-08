package uz.practise.acu.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import uz.practise.acu.domain.entity.user.UserEntity;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "card")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity extends BaseEntity {
    private Double balance;
    private String cardNumber;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
