package ma.enset.mustaphaaarab.examjee.dtos;

import jakarta.validation.constraints.NotBlank;

public record AgencyRequestDTO(
        @NotBlank String nom,
        @NotBlank String adresse,
        @NotBlank String ville,
        @NotBlank String telephone
) {
}
