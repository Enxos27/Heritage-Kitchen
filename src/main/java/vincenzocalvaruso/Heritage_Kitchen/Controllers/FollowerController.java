package vincenzocalvaruso.Heritage_Kitchen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vincenzocalvaruso.Heritage_Kitchen.Service.FollowService;
import vincenzocalvaruso.Heritage_Kitchen.Service.RecipeService;
import vincenzocalvaruso.Heritage_Kitchen.Service.UserService;
import vincenzocalvaruso.Heritage_Kitchen.entity.User;
import vincenzocalvaruso.Heritage_Kitchen.payloads.FollowNotificationDTO;
import vincenzocalvaruso.Heritage_Kitchen.payloads.UserSocialStatsDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/social")
public class FollowerController {

    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;

    // POST /social/follow/{userId} -> Segui o Smetti di seguire
    @Transactional
    @PostMapping("/follow/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void follow(@PathVariable UUID userId, @AuthenticationPrincipal User currentUser) {
        User userToFollow = userService.findById(userId);
        followService.toggleFollow(currentUser, userToFollow);
    }

    // GET /social/stats/{userId} -> Quanti follower ha un utente?
    @GetMapping("/stats/{userId}")
    public UserSocialStatsDTO getStats(@PathVariable UUID userId) {
        User user = userService.findById(userId);

        return new UserSocialStatsDTO(
                followService.getFollowersCount(user),
                followService.getFollowingCount(user),
                recipeService.findByUser(user).size() // Un'info in più che fa comodo al profilo
        );
    }

    @GetMapping("/notifications/follows")
    public List<FollowNotificationDTO> getFollowNotifications(@AuthenticationPrincipal User currentUser) {
        return followService.getRecentFollowers(currentUser);
    }
}