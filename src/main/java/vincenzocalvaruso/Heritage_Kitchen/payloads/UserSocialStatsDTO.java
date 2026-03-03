package vincenzocalvaruso.Heritage_Kitchen.payloads;

public record UserSocialStatsDTO(
        long followersCount,
        long followingCount,
        long recipesCount // quante ricette ha creato
) {
}