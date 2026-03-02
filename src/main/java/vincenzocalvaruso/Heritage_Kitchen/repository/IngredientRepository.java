package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Ingredient;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    List<Ingredient> findByRecipe(Recipe recipe);
}