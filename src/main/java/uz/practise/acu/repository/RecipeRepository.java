package uz.practise.acu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.practise.acu.domain.entity.RecipeEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, UUID> {
    @Query("select r from recipe r where " +
            "r.description ilike %:keyword% or " +
            "CAST(r.category as string) ilike %:keyword% or " +
            "r.name ilike %:keyword% and r.isNotDeleted = true")
    List<RecipeEntity> findByKeyword(@Param("keyword") String keyword);

    @Query("select r from recipe r where r.isNotDeleted = true")
    List<RecipeEntity> findAllByIsDeleted();


}
