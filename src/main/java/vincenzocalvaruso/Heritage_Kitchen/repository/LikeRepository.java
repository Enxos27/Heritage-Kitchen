package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Like;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    long countByRecipe(Recipe recipe); // Quanti like ha una ricetta

    boolean existsByUserAndRecipe(User user, Recipe recipe);  // L'utente ha già messo like?

    void deleteByUserAndRecipe(User user, Recipe recipe);

    void deleteByRecipe(Recipe recipe);

    // like di un utente specifico
    List<Like> findByUser(User user);

    // Trova i like fatti alle ricette dell'utente loggato, ordinati per i più recenti
    @Query("SELECT l FROM Like l WHERE l.recipe.user.id = :userId ORDER BY l.createdAt DESC")
    List<Like> findRecentLikesForUser(@Param("userId") UUID userId);

    long countByRecipeId(UUID recipeId);
}