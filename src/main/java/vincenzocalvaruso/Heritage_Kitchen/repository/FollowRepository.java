package vincenzocalvaruso.Heritage_Kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzocalvaruso.Heritage_Kitchen.entity.Follow;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {
    List<Follow> findByFollower(User follower); // Chi seguo?

    List<Follow> findByFollowing(User following); // Chi mi segue?

    // Esiste già il legame?
    boolean existsByFollowerAndFollowing(User follower, User following);

    // Smetti di seguire
    void deleteByFollowerAndFollowing(User follower, User following);

    // Conta i follower e i seguiti
    long countByFollower(User follower);

    long countByFollowing(User following);

    // Recupera gli ultimi follower di un utente specifico
    // Ordiniamo per data decrescente per mostrare le notifiche più recenti in alto
    List<Follow> findByFollowingOrderByCreatedAtDesc(User following);
}