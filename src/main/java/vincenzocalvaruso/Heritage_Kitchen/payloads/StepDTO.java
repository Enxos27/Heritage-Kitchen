package vincenzocalvaruso.Heritage_Kitchen.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record StepDTO(
        @NotBlank
        @Positive(message = "L'ordine degli step deve essere un numero positivo")
        int ordine,
        @NotBlank
        String descrizione
) {
}
