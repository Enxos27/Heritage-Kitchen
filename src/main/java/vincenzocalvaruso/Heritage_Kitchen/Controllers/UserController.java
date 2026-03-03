package vincenzocalvaruso.Heritage_Kitchen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincenzocalvaruso.Heritage_Kitchen.Service.UserService;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.ValidationException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.LoginDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.LoginResponseDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.UserDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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
        return new LoginResponseDTO(this.userService.authenticateUserAndGenerateToken(body));
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

    //TODO: ATTENZIONE creare dto per ricezioni dati utente
    // pubblico, non devono essere passate email e password

    // PROFILO DI UN ALTRO (Pubblico)
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.findById(id);
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
}
