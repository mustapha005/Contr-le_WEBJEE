package ma.enset.mustaphaaarab.examjee.controllers;

import ma.enset.mustaphaaarab.examjee.dtos.DashboardStatsDTO;
import ma.enset.mustaphaaarab.examjee.services.RentalManagementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final RentalManagementService service;

    public DashboardController(RentalManagementService service) {
        this.service = service;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('EMPLOYE','ADMIN')")
    public DashboardStatsDTO stats() {
        return service.getDashboardStats();
    }
}
