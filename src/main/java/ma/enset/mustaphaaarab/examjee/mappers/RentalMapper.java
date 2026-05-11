package ma.enset.mustaphaaarab.examjee.mappers;

import ma.enset.mustaphaaarab.examjee.dtos.RentalResponseDTO;
import ma.enset.mustaphaaarab.examjee.entities.Rental;
import ma.enset.mustaphaaarab.examjee.entities.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {
    public RentalResponseDTO toDto(Rental rental) {
        Vehicle vehicle = rental.getVehicle();
        String label = vehicle.getMarque() + " " + vehicle.getModele();
        return new RentalResponseDTO(
                rental.getId(),
                vehicle.getId(),
                label,
                vehicle.getMatricule(),
                vehicle.getType(),
                vehicle.getAgency() == null ? null : vehicle.getAgency().getNom(),
                rental.getClientName(),
                rental.getClientEmail(),
                rental.getDateDebut(),
                rental.getDateFin(),
                rental.getTotalPrice(),
                rental.getStatus(),
                rental.getCreatedAt()
        );
    }
}
