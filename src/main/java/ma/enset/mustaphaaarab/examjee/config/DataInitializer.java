package ma.enset.mustaphaaarab.examjee.config;

import ma.enset.mustaphaaarab.examjee.entities.Vehicle;
import ma.enset.mustaphaaarab.examjee.entities.Agency;
import ma.enset.mustaphaaarab.examjee.entities.AppUser;
import ma.enset.mustaphaaarab.examjee.entities.Car;
import ma.enset.mustaphaaarab.examjee.entities.Motorcycle;
import ma.enset.mustaphaaarab.examjee.entities.Rental;
import ma.enset.mustaphaaarab.examjee.enums.FuelType;
import ma.enset.mustaphaaarab.examjee.enums.GearBox;
import ma.enset.mustaphaaarab.examjee.enums.MotorcycleType;
import ma.enset.mustaphaaarab.examjee.enums.RentalStatus;
import ma.enset.mustaphaaarab.examjee.enums.UserRole;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.repositories.AgencyRepository;
import ma.enset.mustaphaaarab.examjee.repositories.RentalRepository;
import ma.enset.mustaphaaarab.examjee.repositories.UserRepository;
import ma.enset.mustaphaaarab.examjee.repositories.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   AgencyRepository agencyRepository,
                                   VehicleRepository vehicleRepository,
                                   RentalRepository rentalRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(user("admin", "admin123", "Administrateur", UserRole.ROLE_ADMIN, passwordEncoder));
                userRepository.save(user("employee", "employee123", "Employe Agence", UserRole.ROLE_EMPLOYE, passwordEncoder));
                userRepository.save(user("client", "client123", "Client Demo", UserRole.ROLE_CLIENT, passwordEncoder));
            }

            if (agencyRepository.count() == 0) {
                Agency casa = agency("ENSET Cars Casa", "Bd Hassan II", "Casablanca", "+212522000111");
                Agency rabat = agency("Rabat Mobility", "Avenue Mohammed V", "Rabat", "+212537000222");
                Agency marrakech = agency("Marrakech Drive", "Gueliz", "Marrakech", "+212524000333");
                agencyRepository.save(casa);
                agencyRepository.save(rabat);
                agencyRepository.save(marrakech);

                Car clio = car("Renault", "Clio 5", "A-111-B", new BigDecimal("320"), LocalDate.of(2022, 5, 10), VehicleStatus.DISPONIBLE, casa, 5, FuelType.DIESEL, GearBox.MANUELLE);
                Car dacia = car("Dacia", "Duster", "B-222-C", new BigDecimal("450"), LocalDate.of(2023, 3, 15), VehicleStatus.LOUE, casa, 5, FuelType.ESSENCE, GearBox.AUTOMATIQUE);
                Car tesla = car("Tesla", "Model 3", "E-777-T", new BigDecimal("900"), LocalDate.of(2024, 1, 18), VehicleStatus.DISPONIBLE, rabat, 5, FuelType.ELECTRIQUE, GearBox.AUTOMATIQUE);
                Motorcycle pcx = motorcycle("Honda", "PCX", "M-333-D", new BigDecimal("180"), LocalDate.of(2021, 8, 4), VehicleStatus.DISPONIBLE, rabat, 125, MotorcycleType.SCOOTER, true);
                Motorcycle mt07 = motorcycle("Yamaha", "MT-07", "M-444-E", new BigDecimal("350"), LocalDate.of(2020, 2, 2), VehicleStatus.EN_MAINTENANCE, marrakech, 689, MotorcycleType.ROADSTER, true);
                vehicleRepository.save(clio);
                vehicleRepository.save(dacia);
                vehicleRepository.save(tesla);
                vehicleRepository.save(pcx);
                vehicleRepository.save(mt07);

                Rental activeRental = rental(dacia, "Sara Amrani", "sara@example.com", LocalDate.now(), LocalDate.now().plusDays(3), new BigDecimal("1800"), RentalStatus.ACTIVE);
                Rental oldRental = rental(clio, "Yassine El Fassi", "yassine@example.com", LocalDate.now().minusDays(10), LocalDate.now().minusDays(8), new BigDecimal("960"), RentalStatus.TERMINEE);
                rentalRepository.save(activeRental);
                rentalRepository.save(oldRental);
            }
        };
    }

    private AppUser user(String username, String password, String fullName, UserRole role, PasswordEncoder encoder) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setFullName(fullName);
        user.setRole(role);
        return user;
    }

    private Agency agency(String nom, String adresse, String ville, String telephone) {
        Agency agency = new Agency();
        agency.setNom(nom);
        agency.setAdresse(adresse);
        agency.setVille(ville);
        agency.setTelephone(telephone);
        return agency;
    }

    private Car car(String marque, String modele, String matricule, BigDecimal prix, LocalDate date, VehicleStatus statut, Agency agency,
                    int doors, FuelType fuel, GearBox gearBox) {
        Car car = new Car();
        car.setMarque(marque);
        car.setModele(modele);
        car.setMatricule(matricule);
        car.setPrixParJour(prix);
        car.setDateMiseEnService(date);
        car.setStatut(statut);
        car.setAgency(agency);
        car.setNombrePortes(doors);
        car.setTypeCarburant(fuel);
        car.setBoiteVitesse(gearBox);
        return car;
    }

    private Motorcycle motorcycle(String marque, String modele, String matricule, BigDecimal prix, LocalDate date, VehicleStatus statut, Agency agency,
                                  int cylindree, MotorcycleType type, boolean casqueInclus) {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setMarque(marque);
        motorcycle.setModele(modele);
        motorcycle.setMatricule(matricule);
        motorcycle.setPrixParJour(prix);
        motorcycle.setDateMiseEnService(date);
        motorcycle.setStatut(statut);
        motorcycle.setAgency(agency);
        motorcycle.setCylindree(cylindree);
        motorcycle.setTypeMoto(type);
        motorcycle.setCasqueInclus(casqueInclus);
        return motorcycle;
    }

    private Rental rental(Vehicle vehicle, String name, String email, LocalDate start, LocalDate end, BigDecimal total, RentalStatus status) {
        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setClientName(name);
        rental.setClientEmail(email);
        rental.setDateDebut(start);
        rental.setDateFin(end);
        rental.setTotalPrice(total);
        rental.setStatus(status);
        return rental;
    }
}
