package vincenzocalvaruso.Heritage_Kitchen.payloads;

import java.time.LocalDateTime;
import java.util.UUID;

public record LikeNotificationDTO(
        String username,
        String avatar,
        String titoloRicetta,
        UUID ricettaId,
        LocalDateTime createdAt
) {
}