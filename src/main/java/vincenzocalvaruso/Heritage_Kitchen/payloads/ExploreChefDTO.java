package vincenzocalvaruso.Heritage_Kitchen.payloads;

import java.util.List;
import java.util.UUID;

public record ExploreChefDTO(
        UUID id,
        String username,
        String avatar,
        String bio,
        boolean isFollowed,
        List<RecipePreviewDTO> recipes
) {
}