package vincenzocalvaruso.Heritage_Kitchen.payloads;

import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RecipeResponseDTO(
        Recipe recipe,
        boolean isLiked,
        boolean isFollowingAuthor,
        Recipe root,
        List<Recipe> variants, //figli
        List<Recipe> siblings, //fratelli
        //La uso per trovare il numero dei nipoti quando mi trovo nella root
        Map<UUID, Long> variantsCounts // <--  ID del figlio -> Numero di nipoti
) {
}