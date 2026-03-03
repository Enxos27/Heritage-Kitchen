package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.Security.JWTTools;
import vincenzocalvaruso.Heritage_Kitchen.entity.Role;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.BadRequestException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotFoundException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.UnauthorizedException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.LoginDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.UserDTO;
import vincenzocalvaruso.Heritage_Kitchen.repository.UserRepository;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTools jwtTools;

    public User registerUser(UserDTO body) {
        if (userRepository.findByEmail(body.email()).isPresent()) {
            throw new BadRequestException("Email già in uso!");
        }
        if (userRepository.findByUsername(body.username()).isPresent()) {
            throw new BadRequestException("Username già in uso!");
        }
        User newUser = new User(
                body.username(),
                passwordEncoder.encode(body.password()),
                body.email(),
                body.bio(),
                body.avatar());
        newUser.setRole(Role.valueOf("USER"));

        return userRepository.save(newUser);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }


    public String authenticateUserAndGenerateToken(LoginDTO dto) {
        // 1. Cerco l'utente nel DB tramite email
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UnauthorizedException("Utente non trovato!"));

        // 2. Verifico se la password fornita (in chiaro) corrisponde a quella nel DB (criptata)
        if (passwordEncoder.matches(dto.password(), user.getPassword())) {

            // 3. Se corrispondono, genero il token
            return jwtTools.generateToken(user);

        } else {
            // 4. Se non corrispondono, lancio un'eccezione
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }
}
