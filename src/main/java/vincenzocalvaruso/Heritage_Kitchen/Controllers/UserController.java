package vincenzocalvaruso.Heritage_Kitchen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
}
