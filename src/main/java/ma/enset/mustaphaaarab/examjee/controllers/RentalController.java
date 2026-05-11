package ma.enset.mustaphaaarab.examjee.controllers;

import jakarta.validation.Valid;
import ma.enset.mustaphaaarab.examjee.dtos.RentalRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RentalResponseDTO;
import ma.enset.mustaphaaarab.examjee.services.RentalManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalManagementService service;

    public RentalController(RentalManagementService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public List<RentalResponseDTO> all() {
        return service.findAllRentals();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public RentalResponseDTO one(@PathVariable Long id) {
        return service.findRentalById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','EMPLOYE','ADMIN')")
    public ResponseEntity<RentalResponseDTO> create(@Valid @RequestBody RentalRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRental(dto));
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public RentalResponseDTO finish(@PathVariable Long id) {
        return service.finishRental(id);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public RentalResponseDTO cancel(@PathVariable Long id) {
        return service.cancelRental(id);
    }
}
