package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.entity.*;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotFoundException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.IngredientDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.RecipeRequestDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.StepDTO;
import vincenzocalvaruso.Heritage_Kitchen.repository.RecipeRepository;
import vincenzocalvaruso.Heritage_Kitchen.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagService tagService;

    public Recipe createRecipe(RecipeRequestDTO dto, User autore) {
        Recipe recipe = new Recipe();
        recipe.setTitolo(dto.titolo());
        recipe.setDescrizione(dto.descrizione());
        recipe.setTempoPrep(dto.tempoPrep());
        recipe.setTempoCottura(dto.tempoCottura());
        recipe.setDifficolta(Difficolta.valueOf(dto.difficolta()));
        recipe.setUser(autore);

        // 1. Gestione Versioning
        if (dto.parentRecipeId() != null) {
            Recipe parent = repository.findById(dto.parentRecipeId())
                    .orElseThrow(() -> new RuntimeException("Ricetta padre non trovata"));
            recipe.setParentRecipe(parent);
        }

        // 2. Trasformazione Ingredienti
        List<Ingredient> ingredientiEntities = new ArrayList<>();
        for (IngredientDTO iDto : dto.ingredienti()) {
            Ingredient ing = new Ingredient();
            ing.setNome(iDto.nome());
            ing.setQuantita(iDto.quantita());
            ing.setRecipe(recipe); // Colleghiamo l'ingrediente alla ricetta
            ingredientiEntities.add(ing);
        }
        recipe.setIngredienti(ingredientiEntities);

        // 3. Trasformazione Step di preparazione
        List<Step> stepEntities = new ArrayList<>();
        for (StepDTO stepDTO : dto.steps()) {
            Step step = new Step();
            step.setOrdine(stepDTO.ordine());
            step.setDescrizione(stepDTO.descrizione());
            step.setRecipe(recipe);
            stepEntities.add(step);
        }
        recipe.setSteps(stepEntities);

        // 4. Gestione Tag
        // Il controllo per i dublicati si trova all'interno di saveTag()
        if (dto.tags() != null) {
            List<Tag> tags = new ArrayList<>();
            for (String tagName : dto.tags()) {
                tags.add(tagService.saveTag(tagName));
            }
            recipe.setTags(tags);
        }

        // 5. Salvataggio finale
        return repository.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }

    public Recipe getRecipeById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ricetta non trovata"));
    }

    public List<Recipe> getOriginalRecipes() {
        return repository.findByParentRecipeIsNull();
    }

    public List<Recipe> findByParentRecipe(Recipe parent) {
        return repository.findByParentRecipe(parent);
    }

    public List<Recipe> findByUser(User user) {
        return repository.findByUser(user);
    }

}
