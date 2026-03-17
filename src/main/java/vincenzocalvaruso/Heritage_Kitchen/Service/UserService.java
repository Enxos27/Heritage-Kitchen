package vincenzocalvaruso.Heritage_Kitchen.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vincenzocalvaruso.Heritage_Kitchen.Security.JWTTools;
import vincenzocalvaruso.Heritage_Kitchen.entity.Role;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.BadRequestException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotEmptyException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotFoundException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.UnauthorizedException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.*;
import vincenzocalvaruso.Heritage_Kitchen.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private Cloudinary cloudinaryUploader;

    // REGISRA UTENTE
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

    // CERCA UTENTE PER ID
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }


    // LOGIN UTENTE + GENERA E RESTITUISCE TOKEN
    // Cambia il tipo di ritorno da String a LoginResponseDTO
    public LoginResponseDTO authenticateUserAndGenerateToken(LoginDTO dto) {
        // 1. Cerco l'utente nel DB tramite username
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide!"));

        // 2. Verifico la password
        if (passwordEncoder.matches(dto.password(), user.getPassword())) {

            // 3. Genero il token
            String accessToken = jwtTools.generateToken(user);

            // 4. Mappo i dati dell'utente nel DTO per il profilo pubblico (Sidebar)
            // Se non hai ancora le stats nel database, puoi passare valori finti o 0 per ora
            UserSocialStatsDTO stats = new UserSocialStatsDTO(0, 0, 0);

            UserPublicProfileDTO userProfile = new UserPublicProfileDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getAvatar(),
                    user.getBio(),
                    stats,
                    false
            );

            // 5. Restituisco l'oggetto completo (con accessToken e user)
            // Assicurati che il record LoginResponseDTO abbia questi nomi!
            return new LoginResponseDTO(accessToken, userProfile);

        } else {
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }

    // ELIMINA UTENTE
    public void deleteUser(UUID id) {
        User u = this.findById(id);
        this.userRepository.delete(u);
    }

    //    UPLOAD AVATAR UTENTE
    public User findByIdAndUploadAvatar(UUID utenteId, MultipartFile file) {
        if (file.isEmpty()) throw new NotEmptyException();
        User found = this.findById(utenteId);
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            found.setAvatar(imageUrl);

            return userRepository.save(found);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateBio(UUID userId, String newBio) {
        User user = findById(userId);
        user.setBio(newBio);
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
