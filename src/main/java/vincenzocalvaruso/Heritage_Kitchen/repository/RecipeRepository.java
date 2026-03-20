package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    // Trova tutte le ricette originali (senza padre)
    List<Recipe> findByParentRecipeIsNull();

    // Trova tutti i "branch" di una specifica ricetta
    List<Recipe> findByParentRecipe(Recipe parent);

    // Trova tutte le ricette di un utente specifico
    List<Recipe> findByUser(User user);


    // Trova tutte le ricette di un utente specifico tramite UUID
    List<Recipe> findByUserId(UUID userId);

    // Ricerca per titolo che contiene la stringa (Case Insensitive)
    List<Recipe> findByTitoloContainingIgnoreCase(String titolo);

    // Trova tutte le ricette che hanno un determinato tag (per nome)
    List<Recipe> findByTags_NomeIgnoreCase(String tagName);

    // Conta quante varianti dirette ha una determinata ricetta
    long countByParentRecipe(Recipe parent);
}