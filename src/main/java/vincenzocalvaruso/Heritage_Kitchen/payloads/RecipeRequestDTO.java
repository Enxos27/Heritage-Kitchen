package vincenzocalvaruso.Heritage_Kitchen.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record RecipeRequestDTO(
        @NotBlank(message = "Il titolo è obbligatorio")
        String titolo,

        @NotBlank(message = "La descrizione è obbligatoria")
        String descrizione,

        @NotBlank(message = "La difficoltà è obbligatoria")
        String difficolta,

        @NotNull(message = "Il tempo di preparazione è obbligatorio")
        @Min(value = 1, message = "Il tempo di preparazione deve essere almeno di 1 minuto")
        int tempoPrep,

        @NotNull(message = "Il tempo di cottura è obbligatorio")
        @Min(value = 0, message = "Il tempo di cottura non può essere negativo")
        int tempoCottura,

        UUID parentRecipeId,

        List<IngredientDTO> ingredienti,
        List<StepDTO> steps,
        List<String> tags
) {
}