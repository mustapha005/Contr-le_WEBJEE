package ma.enset.mustaphaaarab.examjee.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentalRequestDTO(
        @NotNull Long vehicleId,
        @NotBlank String clientName,
        @Email @NotBlank String clientEmail,
        @NotNull LocalDate dateDebut,
        @NotNull LocalDate dateFin
) {
}
