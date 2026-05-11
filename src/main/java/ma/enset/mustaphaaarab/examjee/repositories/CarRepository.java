package ma.enset.mustaphaaarab.examjee.repositories;

import ma.enset.mustaphaaarab.examjee.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
