package vincenzocalvaruso.Heritage_Kitchen.payloads;

import java.util.UUID;

public record RecipePreviewDTO(
        UUID id,
        String titolo,
        String imageURL
) {
}