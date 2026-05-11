package ma.enset.mustaphaaarab.examjee.repositories;

import ma.enset.mustaphaaarab.examjee.entities.Rental;
import ma.enset.mustaphaaarab.examjee.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    long countByStatus(RentalStatus status);

    @Query("select r from Rental r join fetch r.vehicle v join fetch v.agency order by r.createdAt desc")
    List<Rental> findAllDetailed();

    List<Rental> findTop5ByOrderByCreatedAtDesc();
}
