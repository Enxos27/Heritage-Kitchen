package vincenzocalvaruso.Heritage_Kitchen.payloads;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record RecipeRequestDTO(
        @NotBlank
        String titolo,
        @NotBlank
        String descrizione,
        @NotBlank
        String difficolta, // facile - medio - difficile
        @NotBlank
        int tempoPrep,
        @NotBlank
        int tempoCottura,

        UUID parentRecipeId, // Il punto chiave per il versioning --> se null è la root, se ha un valore è un "fork"

        List<IngredientDTO> ingredienti,
        List<StepDTO> steps
) {
}
