package vincenzocalvaruso.Heritage_Kitchen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vincenzocalvaruso.Heritage_Kitchen.Service.LikeService;
import vincenzocalvaruso.Heritage_Kitchen.Service.RecipeService;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private RecipeService recipeService;

    // POST /likes/recipe/{recipeId} -> Clicco sul cuore
    @PostMapping("/recipe/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleRecipeLike(@PathVariable UUID recipeId, @AuthenticationPrincipal User currentUser) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        likeService.toggleLike(currentUser, recipe);
    }

    // GET /likes/me -> Le mie ricette preferite
    @GetMapping("/me")
    public List<Recipe> getMyFavorites(@AuthenticationPrincipal User currentUser) {
        return likeService.getUserFavorites(currentUser);
    }
}