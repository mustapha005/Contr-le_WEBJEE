package ma.enset.mustaphaaarab.examjee.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank String username,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String fullName
) {
}
