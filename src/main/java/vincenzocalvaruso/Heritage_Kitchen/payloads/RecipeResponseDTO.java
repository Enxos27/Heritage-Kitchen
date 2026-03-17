package vincenzocalvaruso.Heritage_Kitchen.payloads;

import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;

public record RecipeResponseDTO(
        Recipe recipe,
        boolean isLiked,
        boolean isFollowingAuthor
) {
}