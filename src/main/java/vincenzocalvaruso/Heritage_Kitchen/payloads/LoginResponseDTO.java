package vincenzocalvaruso.Heritage_Kitchen.payloads;

public record LoginResponseDTO(String accessToken, UserPublicProfileDTO user) {
}