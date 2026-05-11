package ma.enset.mustaphaaarab.examjee.repositories;

import ma.enset.mustaphaaarab.examjee.entities.Vehicle;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByMatriculeIgnoreCase(String matricule);
    boolean existsByMatriculeIgnoreCase(String matricule);
    long countByStatut(VehicleStatus statut);
    List<Vehicle> findTop5ByOrderByIdDesc();

    @Query("select count(v) from Vehicle v where type(v) = Car")
    long countCars();

    @Query("select count(v) from Vehicle v where type(v) = Motorcycle")
    long countMotorcycles();
}
