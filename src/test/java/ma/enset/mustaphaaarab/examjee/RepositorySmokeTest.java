package ma.enset.mustaphaaarab.examjee;

import ma.enset.mustaphaaarab.examjee.entities.Agency;
import ma.enset.mustaphaaarab.examjee.entities.Car;
import ma.enset.mustaphaaarab.examjee.enums.FuelType;
import ma.enset.mustaphaaarab.examjee.enums.GearBox;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.repositories.AgencyRepository;
import ma.enset.mustaphaaarab.examjee.repositories.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositorySmokeTest {
    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void shouldPersistAgencyAndCar() {
        Agency agency = new Agency();
        agency.setNom("Test Agency");
        agency.setAdresse("Rue 1");
        agency.setVille("Mohammedia");
        agency.setTelephone("0600000000");
        agency = agencyRepository.save(agency);

        Car car = new Car();
        car.setMarque("Peugeot");
        car.setModele("208");
        car.setMatricule("TEST-001");
        car.setPrixParJour(new BigDecimal("300"));
        car.setDateMiseEnService(LocalDate.now());
        car.setStatut(VehicleStatus.DISPONIBLE);
        car.setAgency(agency);
        car.setNombrePortes(5);
        car.setTypeCarburant(FuelType.ESSENCE);
        car.setBoiteVitesse(GearBox.MANUELLE);

        vehicleRepository.save(car);

        assertThat(vehicleRepository.findByMatriculeIgnoreCase("test-001")).isPresent();
        assertThat(vehicleRepository.countByStatut(VehicleStatus.DISPONIBLE)).isEqualTo(1);
    }
}
