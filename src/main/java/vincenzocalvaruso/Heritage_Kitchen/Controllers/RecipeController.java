package vincenzocalvaruso.Heritage_Kitchen.Controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincenzocalvaruso.Heritage_Kitchen.Service.RecipeService;
import vincenzocalvaruso.Heritage_Kitchen.Service.UserService;
import vincenzocalvaruso.Heritage_Kitchen.entity.Recipe;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.payloads.RecipeRequestDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.RecipeResponseDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    // 1. CREAZIONE (Originale o Versionata)
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe createRecipe(
            @RequestBody @Valid RecipeRequestDTO body,
            @AuthenticationPrincipal User currentUser
    ) {
        // currentUser viene iniettato automaticamente dal tuo JWTCheckerFilter
        return recipeService.createRecipe(body, currentUser);
    }

    // 2. TUTTE LE RICETTE (Per la ricerca globale)

    @GetMapping
    public Page<Recipe> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return recipeService.findAll(pageable);
    }

    // 3. SOLO LE ORIGINALI (Per la bacheca principale / home)
    @GetMapping("/originals")
    public List<Recipe> getOriginals() {
        return recipeService.getOriginalRecipes();
    }

    // 4. DETTAGLIO RICETTA
    @GetMapping("/{id}")
    public RecipeResponseDTO getById(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        return recipeService.getRecipeDetail(id, currentUser);
    }

    // 5. TUTTE LE VERSIONI DI UNA RICETTA SPECIFICA (Versioning Explorer)
    @GetMapping("/{id}/versions")
    public List<Recipe> getVersions(@PathVariable UUID id) {
        Recipe parent = recipeService.getRecipeById(id);
        return recipeService.findByParentRecipe(parent);
    }

    // 6. RICETTE DI UN DETERMINATO UTENTE (Per la pagina profilo)
    @GetMapping("/user/{userId}")
    public List<Recipe> getByUser(@PathVariable UUID userId) {
        User user = userService.findById(userId);
        return recipeService.findByUser(user);
    }

    // 7. AGGIUNGI IMMAGINE A RICETTA; CONTROLLO NEL SERVICE
    @PatchMapping("/{id}/image")
    public Recipe uploadRecipeImage(
            @PathVariable UUID id,
            @RequestParam("immagine") MultipartFile file,
            @AuthenticationPrincipal User currentUser
    ) {
        return this.recipeService.findByIdAndUploadImage(id, file, currentUser);
    }

    // 8. MODIFICA (PUT)
    @PutMapping("/{id}")
    public Recipe update(@PathVariable UUID id,
                         @RequestBody @Valid RecipeRequestDTO body,
                         @AuthenticationPrincipal User currentUser) {
        return recipeService.updateRecipe(id, body, currentUser);
    }

    // 9. ELIMINAZIONE (DELETE)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        recipeService.deleteRecipe(id, currentUser);
    }

    // 10. RICERCA (GET con Query Parameter)
    // Esempio: /recipes/search?titolo=cannolo
    @GetMapping("/search")
    public List<Recipe> search(@RequestParam String titolo) {
        return recipeService.searchRecipes(titolo);
    }

    // 11. FILTRA PER TAG
    // Esempio: /recipes/tag/Sicilia
    @GetMapping("/tag/{tagName}")
    public List<Recipe> getByTag(@PathVariable String tagName) {
        return recipeService.findRecipesByTag(tagName);
    }

    @GetMapping("/me/liked")
    public List<Recipe> getMyLikedRecipes(@AuthenticationPrincipal User currentUser) {
        return recipeService.findLikedRecipesByUser(currentUser);
    }
}