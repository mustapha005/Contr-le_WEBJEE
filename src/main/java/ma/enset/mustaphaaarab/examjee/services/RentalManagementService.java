package ma.enset.mustaphaaarab.examjee.services;

import ma.enset.mustaphaaarab.examjee.dtos.AgencyRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.AgencyResponseDTO;
import ma.enset.mustaphaaarab.examjee.dtos.DashboardStatsDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RentalRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RentalResponseDTO;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleResponseDTO;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.enums.VehicleType;

import java.util.List;

public interface RentalManagementService {
    List<AgencyResponseDTO> findAllAgencies();
    AgencyResponseDTO findAgencyById(Long id);
    AgencyResponseDTO createAgency(AgencyRequestDTO dto);
    AgencyResponseDTO updateAgency(Long id, AgencyRequestDTO dto);
    void deleteAgency(Long id);

    List<VehicleResponseDTO> searchVehicles(VehicleStatus status, VehicleType type, Long agencyId, String keyword);
    VehicleResponseDTO findVehicleById(Long id);
    VehicleResponseDTO createVehicle(VehicleRequestDTO dto);
    VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO dto);
    void deleteVehicle(Long id);

    List<RentalResponseDTO> findAllRentals();
    RentalResponseDTO findRentalById(Long id);
    RentalResponseDTO createRental(RentalRequestDTO dto);
    RentalResponseDTO finishRental(Long id);
    RentalResponseDTO cancelRental(Long id);

    DashboardStatsDTO getDashboardStats();
}
