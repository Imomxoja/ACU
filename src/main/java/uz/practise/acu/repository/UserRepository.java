package uz.practise.acu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
     Optional<UserEntity> findByEmail(String email);

     @Query("select u.savedRecipes from users u where u.id = :userId")
     List<RecipeEntity> giveAllInSaved(@Param("userId") UUID userId);
}
