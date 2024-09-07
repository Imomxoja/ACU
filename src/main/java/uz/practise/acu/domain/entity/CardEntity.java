package uz.practise.acu.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.practise.acu.domain.entity.user.UserEntity;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity extends BaseEntity {
    private Double balance;
    private String cardNumber;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
