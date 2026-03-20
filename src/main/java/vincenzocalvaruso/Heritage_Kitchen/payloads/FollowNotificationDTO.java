package vincenzocalvaruso.Heritage_Kitchen.payloads;

import java.time.LocalDateTime;
import java.util.UUID;

public record FollowNotificationDTO(
        UUID followerId,      // ID per cliccare e andare al profilo
        String username,      // Chi ha iniziato a seguirti
        String avatar,        // La sua foto
        LocalDateTime createdAt  // Quando è successo
) {
}
