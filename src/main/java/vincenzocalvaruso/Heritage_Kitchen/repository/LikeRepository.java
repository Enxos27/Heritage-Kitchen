package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Like;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    long countByRecipe(Recipe recipe); // Quanti like ha una ricetta

    boolean existsByUserAndRecipe(User user, Recipe recipe);  // L'utente ha già messo like?

    void deleteByUserAndRecipe(User user, Recipe recipe);
}