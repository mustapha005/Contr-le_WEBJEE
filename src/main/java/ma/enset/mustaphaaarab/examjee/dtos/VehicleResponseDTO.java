package ma.enset.mustaphaaarab.examjee.dtos;

import ma.enset.mustaphaaarab.examjee.enums.FuelType;
import ma.enset.mustaphaaarab.examjee.enums.GearBox;
import ma.enset.mustaphaaarab.examjee.enums.MotorcycleType;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VehicleResponseDTO(
        Long id,
        String marque,
        String modele,
        String matricule,
        BigDecimal prixParJour,
        LocalDate dateMiseEnService,
        VehicleStatus statut,
        Long agencyId,
        String agencyName,
        VehicleType type,
        Integer nombrePortes,
        FuelType typeCarburant,
        GearBox boiteVitesse,
        Integer cylindree,
        MotorcycleType typeMoto,
        Boolean casqueInclus
) {
}
