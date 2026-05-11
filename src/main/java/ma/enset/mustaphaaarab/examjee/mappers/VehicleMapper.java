package ma.enset.mustaphaaarab.examjee.mappers;

import ma.enset.mustaphaaarab.examjee.enums.FuelType;
import ma.enset.mustaphaaarab.examjee.enums.GearBox;
import ma.enset.mustaphaaarab.examjee.enums.MotorcycleType;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleResponseDTO;
import ma.enset.mustaphaaarab.examjee.entities.Agency;
import ma.enset.mustaphaaarab.examjee.entities.Car;
import ma.enset.mustaphaaarab.examjee.entities.Motorcycle;
import ma.enset.mustaphaaarab.examjee.entities.Vehicle;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.exceptions.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    public VehicleResponseDTO toDto(Vehicle vehicle) {
        Integer nombrePortes = null;
        var typeCarburant = (FuelType) null;
        var boiteVitesse = (GearBox) null;
        Integer cylindree = null;
        var typeMoto = (MotorcycleType) null;
        Boolean casqueInclus = null;

        if (vehicle instanceof Car car) {
            nombrePortes = car.getNombrePortes();
            typeCarburant = car.getTypeCarburant();
            boiteVitesse = car.getBoiteVitesse();
        } else if (vehicle instanceof Motorcycle motorcycle) {
            cylindree = motorcycle.getCylindree();
            typeMoto = motorcycle.getTypeMoto();
            casqueInclus = motorcycle.getCasqueInclus();
        }

        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getMarque(),
                vehicle.getModele(),
                vehicle.getMatricule(),
                vehicle.getPrixParJour(),
                vehicle.getDateMiseEnService(),
                vehicle.getStatut(),
                vehicle.getAgency() == null ? null : vehicle.getAgency().getId(),
                vehicle.getAgency() == null ? null : vehicle.getAgency().getNom(),
                vehicle.getType(),
                nombrePortes,
                typeCarburant,
                boiteVitesse,
                cylindree,
                typeMoto,
                casqueInclus
        );
    }

    public Vehicle toEntity(VehicleRequestDTO dto, Agency agency) {
        Vehicle vehicle = switch (dto.type()) {
            case VOITURE -> {
                validateCar(dto);
                Car car = new Car();
                car.setNombrePortes(dto.nombrePortes());
                car.setTypeCarburant(dto.typeCarburant());
                car.setBoiteVitesse(dto.boiteVitesse());
                yield car;
            }
            case MOTO -> {
                validateMotorcycle(dto);
                Motorcycle motorcycle = new Motorcycle();
                motorcycle.setCylindree(dto.cylindree());
                motorcycle.setTypeMoto(dto.typeMoto());
                motorcycle.setCasqueInclus(Boolean.TRUE.equals(dto.casqueInclus()));
                yield motorcycle;
            }
        };
        applyCommonFields(vehicle, dto, agency);
        return vehicle;
    }

    public void updateEntity(Vehicle vehicle, VehicleRequestDTO dto, Agency agency) {
        if (vehicle.getType() != dto.type()) {
            throw new BusinessException("Le changement du type de vehicule n'est pas autorise dans cette operation");
        }
        applyCommonFields(vehicle, dto, agency);
        if (vehicle instanceof Car car) {
            validateCar(dto);
            car.setNombrePortes(dto.nombrePortes());
            car.setTypeCarburant(dto.typeCarburant());
            car.setBoiteVitesse(dto.boiteVitesse());
        } else if (vehicle instanceof Motorcycle motorcycle) {
            validateMotorcycle(dto);
            motorcycle.setCylindree(dto.cylindree());
            motorcycle.setTypeMoto(dto.typeMoto());
            motorcycle.setCasqueInclus(Boolean.TRUE.equals(dto.casqueInclus()));
        }
    }

    private void applyCommonFields(Vehicle vehicle, VehicleRequestDTO dto, Agency agency) {
        vehicle.setMarque(dto.marque());
        vehicle.setModele(dto.modele());
        vehicle.setMatricule(dto.matricule());
        vehicle.setPrixParJour(dto.prixParJour());
        vehicle.setDateMiseEnService(dto.dateMiseEnService());
        vehicle.setStatut(dto.statut() == null ? VehicleStatus.DISPONIBLE : dto.statut());
        vehicle.setAgency(agency);
    }

    private void validateCar(VehicleRequestDTO dto) {
        if (dto.nombrePortes() == null || dto.typeCarburant() == null || dto.boiteVitesse() == null) {
            throw new BusinessException("Une voiture doit avoir nombrePortes, typeCarburant et boiteVitesse");
        }
    }

    private void validateMotorcycle(VehicleRequestDTO dto) {
        if (dto.cylindree() == null || dto.typeMoto() == null) {
            throw new BusinessException("Une moto doit avoir cylindree et typeMoto");
        }
    }
}
