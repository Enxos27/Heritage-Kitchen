package vincenzocalvaruso.Heritage_Kitchen.payloads;

import jakarta.validation.constraints.NotBlank;

public record IngredientDTO(
        @NotBlank
        String nome,
        @NotBlank
        String quantita
) {
}
