package vincenzocalvaruso.Heritage_Kitchen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.userService.authenticateUserAndGenerateToken(body));
    }

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        // Grazie al JWTCheckerFilter, l'utente è già nel contesto di sicurezza
        return currentUser;
    }

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
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.findById(id);
    }
}
