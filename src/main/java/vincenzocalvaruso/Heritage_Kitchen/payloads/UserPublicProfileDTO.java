package vincenzocalvaruso.Heritage_Kitchen.payloads;

import java.util.UUID;

public record UserPublicProfileDTO(
        UUID id,
        String username,
        String avatar,
        String bio,
        UserSocialStatsDTO stats,
        boolean isFollowedByMe
) {
}