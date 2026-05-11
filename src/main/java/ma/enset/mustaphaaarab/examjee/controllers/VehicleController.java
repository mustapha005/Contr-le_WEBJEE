package ma.enset.mustaphaaarab.examjee.controllers;

import jakarta.validation.Valid;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleResponseDTO;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.enums.VehicleType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final RentalManagementService service;

    public VehicleController(RentalManagementService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT','EMPLOYE','ADMIN')")
    public List<VehicleResponseDTO> search(@RequestParam(required = false) VehicleStatus status,
                                           @RequestParam(required = false) VehicleType type,
                                           @RequestParam(required = false) Long agencyId,
                                           @RequestParam(required = false) String keyword) {
        return service.searchVehicles(status, type, agencyId, keyword);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','EMPLOYE','ADMIN')")
    public VehicleResponseDTO one(@PathVariable Long id) {
        return service.findVehicleById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public ResponseEntity<VehicleResponseDTO> create(@Valid @RequestBody VehicleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createVehicle(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public VehicleResponseDTO update(@PathVariable Long id, @Valid @RequestBody VehicleRequestDTO dto) {
        return service.updateVehicle(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
