package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.entity.Follow;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.exceptions.BadRequestException;
import vincenzocalvaruso.Heritage_Kitchen.repository.FollowRepository;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public void toggleFollow(User follower, User following) {
        if (follower.getId().equals(following.getId())) {
            throw new BadRequestException("Non puoi seguire te stesso!");
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            // Se lo seguo già, faccio unfollow
            followRepository.deleteByFollowerAndFollowing(follower, following);
        } else {
            // Altrimenti, inizio a seguirlo
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
        }
    }

    // Metodi per le statistiche (da mostrare nel profilo)
    public long getFollowersCount(User user) {
        return followRepository.countByFollowing(user);
    }

    public long getFollowingCount(User user) {
        return followRepository.countByFollower(user);
    }
}