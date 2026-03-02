package vincenzocalvaruso.Heritage_Kitchen.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.Heritage_Kitchen.entity.Follow;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.repository.FollowRepository;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public void followUser(User follower, User following) {
        if (!follower.equals(following)) { // Non posso seguire me stesso
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
        }
    }
}
