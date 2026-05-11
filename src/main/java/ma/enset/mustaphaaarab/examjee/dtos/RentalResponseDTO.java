package ma.enset.mustaphaaarab.examjee.dtos;

import ma.enset.mustaphaaarab.examjee.enums.RentalStatus;
import ma.enset.mustaphaaarab.examjee.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RentalResponseDTO(
        Long id,
        Long vehicleId,
        String vehicleLabel,
        String matricule,
        VehicleType vehicleType,
        String agencyName,
        String clientName,
        String clientEmail,
        LocalDate dateDebut,
        LocalDate dateFin,
        BigDecimal totalPrice,
        RentalStatus status,
        LocalDateTime createdAt
) {
}
