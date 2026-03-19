package vincenzocalvaruso.Heritage_Kitchen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincenzocalvaruso.Heritage_Kitchen.Service.FollowService;
import vincenzocalvaruso.Heritage_Kitchen.Service.RecipeService;
import vincenzocalvaruso.Heritage_Kitchen.Service.UserService;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.ValidationException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;
    @Autowired
    private RecipeService recipeService;

    // REGISTRAZIONE UTENTE
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated UserDTO dto, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.userService.registerUser(dto);
        }
    }

    // LOGIN UTENTE
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        // Ora il service restituisce l'oggetto completo, non solo la stringa
        return userService.authenticateUserAndGenerateToken(body);
    }

    // IL MIO PROFILO (Privato)
    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        // Grazie al JWTCheckerFilter, l'utente è già nel contesto di sicurezza
        return currentUser;
    }

    // ELIMINA UTENTE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    //#id == authentication.principal.id
    //serve per dare la possibilità di eliminare l'account al proprietario
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    //    GET PROFILO DI UN ALTRO UTENTE
    @GetMapping("/{id}/profile")
    public UserPublicProfileDTO getUserProfile(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        User user = userService.findById(id);

        // Controlla il follow
        boolean followedByMe = followService.isFollowing(currentUser, user);

        UserSocialStatsDTO stats = new UserSocialStatsDTO(
                followService.getFollowersCount(user),
                followService.getFollowingCount(user),
                recipeService.findByUser(user).size()
        );

        return new UserPublicProfileDTO(user.getId(), user.getUsername(), user.getAvatar(), user.getBio(), stats, followedByMe);
    }

    // PATCH /user/me/avatar
    // CARICO AVATAR; RECUPERO UTENTE DAL TOKEN
    @PatchMapping("/me/avatar")
    public User uploadMyAvatar(
            @RequestParam("avatar") MultipartFile file,
            @AuthenticationPrincipal User currentUser // Recupera l'utente dal Token
    ) {
        // Uso l'ID dell'utente loggato, garantendo la sicurezza al 100%
        return this.userService.findByIdAndUploadAvatar(currentUser.getId(), file);
    }

    @PutMapping("/me/bio")
    public User updateMyBio(@AuthenticationPrincipal User currentUser, @RequestBody String newBio) {
        // Nota: Se invii solo una stringa cruda, il body è il testo stesso
        return userService.updateBio(currentUser.getId(), newBio);
    }

    @GetMapping("/suggestions")
    public List<User> getSuggestions(@AuthenticationPrincipal User currentUser) {
        return userService.findAllUsers().stream()
                .filter(user -> !user.getId().equals(currentUser.getId())) //escludo me stesso
                .limit(5) // Ne prendo solo 5 per la sidebar
                .toList();
    }

    @GetMapping("/explore")
    public Slice<ExploreChefDTO> getExplore(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable // Spring gestisce automaticamente page, size e sort dai parametri URL
    ) {
        return userService.getExplorePage(currentUser, pageable);
    }
}
