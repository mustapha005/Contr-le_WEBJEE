package ma.enset.mustaphaaarab.examjee.controllers;

import jakarta.validation.Valid;
import ma.enset.mustaphaaarab.examjee.dtos.AgencyRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.AgencyResponseDTO;
import ma.enset.mustaphaaarab.examjee.services.RentalManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agencies")
public class AgencyController {
    private final RentalManagementService service;

    public AgencyController(RentalManagementService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT','EMPLOYE','ADMIN')")
    public List<AgencyResponseDTO> all() {
        return service.findAllAgencies();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','EMPLOYE','ADMIN')")
    public AgencyResponseDTO one(@PathVariable Long id) {
        return service.findAgencyById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public ResponseEntity<AgencyResponseDTO> create(@Valid @RequestBody AgencyRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAgency(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public AgencyResponseDTO update(@PathVariable Long id, @Valid @RequestBody AgencyRequestDTO dto) {
        return service.updateAgency(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAgency(id);
        return ResponseEntity.noContent().build();
    }
}
