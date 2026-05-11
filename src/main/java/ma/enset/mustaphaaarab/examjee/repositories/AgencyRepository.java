package ma.enset.mustaphaaarab.examjee.repositories;

import ma.enset.mustaphaaarab.examjee.entities.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    boolean existsByNomIgnoreCase(String nom);
}
