package uz.practise.acu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.practise.acu.domain.entity.CardEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    Optional<CardEntity> findByCardNumber(String cardNumber);
}
