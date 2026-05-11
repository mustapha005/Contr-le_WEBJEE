package ma.enset.mustaphaaarab.examjee.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank String username,
        @NotBlank String password
) {
}
