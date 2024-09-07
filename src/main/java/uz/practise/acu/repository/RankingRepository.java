package uz.practise.acu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.practise.acu.domain.entity.reaction.RankingEntity;

import java.util.UUID;

@Repository
public interface RankingRepository extends JpaRepository<RankingEntity, UUID> {
}
