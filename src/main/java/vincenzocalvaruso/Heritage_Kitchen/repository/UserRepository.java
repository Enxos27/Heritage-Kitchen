package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    // Recupera una porzione di utenti escludendo l'ID specificato
    Slice<User> findAllByIdNot(UUID userId, Pageable pageable);
}