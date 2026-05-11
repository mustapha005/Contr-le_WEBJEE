package ma.enset.mustaphaaarab.examjee.dtos;

public record AgencyResponseDTO(
        Long id,
        String nom,
        String adresse,
        String ville,
        String telephone,
        int vehicleCount
) {
}
