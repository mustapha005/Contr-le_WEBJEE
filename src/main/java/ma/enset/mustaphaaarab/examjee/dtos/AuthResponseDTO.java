package ma.enset.mustaphaaarab.examjee.dtos;

import ma.enset.mustaphaaarab.examjee.enums.UserRole;

public record AuthResponseDTO(
        String token,
        String tokenType,
        String username,
        String fullName,
        UserRole role
) {
}
