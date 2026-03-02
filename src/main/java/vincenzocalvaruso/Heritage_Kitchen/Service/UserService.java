package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.entity.Role;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.BadRequestException;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.NotFoundException;
import vincenzocalvaruso.Heritage_Kitchen.payloads.UserDTO;
import vincenzocalvaruso.Heritage_Kitchen.repository.UserRepository;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//TODO: criptare la password implementando la security

    public User registerUser(UserDTO body) {
        if (userRepository.findByEmail(body.email()).isPresent()) {
            throw new BadRequestException("Email già in uso!");
        }
        if (userRepository.findByUsername(body.username()).isPresent()) {
            throw new BadRequestException("Username già in uso!");
        }
        User newUser = new User(
                body.username(),
                body.password(),
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
}
