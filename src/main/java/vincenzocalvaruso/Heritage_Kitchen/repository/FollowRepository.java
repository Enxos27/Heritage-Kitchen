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
}