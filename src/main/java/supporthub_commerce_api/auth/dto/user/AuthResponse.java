package supporthub_commerce_api.auth.dto.user;

import supporthub_commerce_api.user.enumsUser.UserRole;

public record AuthResponse(
    String token,
    Long id,
    String name,
    String email,
    UserRole role
) {
}
