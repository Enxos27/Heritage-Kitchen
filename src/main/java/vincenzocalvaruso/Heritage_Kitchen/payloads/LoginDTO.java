package vincenzocalvaruso.Heritage_Kitchen.payloads;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "Lo username è obbligatorio")
        String username,
        @NotBlank(message = "La password è obbligatoria")
        String password
) {
}
