package ma.enset.mustaphaaarab.examjee.services.impl;

import ma.enset.mustaphaaarab.examjee.dtos.AgencyRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.AgencyResponseDTO;
import ma.enset.mustaphaaarab.examjee.dtos.DashboardStatsDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RentalRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RentalResponseDTO;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.VehicleResponseDTO;
import ma.enset.mustaphaaarab.examjee.entities.Agency;
import ma.enset.mustaphaaarab.examjee.entities.Rental;
import ma.enset.mustaphaaarab.examjee.entities.Vehicle;
import ma.enset.mustaphaaarab.examjee.enums.RentalStatus;
import ma.enset.mustaphaaarab.examjee.enums.VehicleStatus;
import ma.enset.mustaphaaarab.examjee.enums.VehicleType;
import ma.enset.mustaphaaarab.examjee.exceptions.BusinessException;
import ma.enset.mustaphaaarab.examjee.exceptions.ResourceNotFoundException;
import ma.enset.mustaphaaarab.examjee.mappers.AgencyMapper;
import ma.enset.mustaphaaarab.examjee.mappers.RentalMapper;
import ma.enset.mustaphaaarab.examjee.mappers.VehicleMapper;
import ma.enset.mustaphaaarab.examjee.repositories.AgencyRepository;
import ma.enset.mustaphaaarab.examjee.repositories.RentalRepository;
import ma.enset.mustaphaaarab.examjee.repositories.VehicleRepository;
import ma.enset.mustaphaaarab.examjee.services.RentalManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class RentalManagementServiceImpl implements RentalManagementService {
    private final AgencyRepository agencyRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    private final AgencyMapper agencyMapper;
    private final VehicleMapper vehicleMapper;
    private final RentalMapper rentalMapper;

    public RentalManagementServiceImpl(AgencyRepository agencyRepository,
                                       VehicleRepository vehicleRepository,
                                       RentalRepository rentalRepository,
                                       AgencyMapper agencyMapper,
                                       VehicleMapper vehicleMapper,
                                       RentalMapper rentalMapper) {
        this.agencyRepository = agencyRepository;
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
        this.agencyMapper = agencyMapper;
        this.vehicleMapper = vehicleMapper;
        this.rentalMapper = rentalMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgencyResponseDTO> findAllAgencies() {
        return agencyRepository.findAll().stream()
                .sorted(Comparator.comparing(Agency::getId).reversed())
                .map(agencyMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AgencyResponseDTO findAgencyById(Long id) {
        return agencyMapper.toDto(getAgency(id));
    }

    @Override
    public AgencyResponseDTO createAgency(AgencyRequestDTO dto) {
        if (agencyRepository.existsByNomIgnoreCase(dto.nom())) {
            throw new BusinessException("Une agence avec ce nom existe deja");
        }
        Agency agency = agencyMapper.toEntity(dto);
        return agencyMapper.toDto(agencyRepository.save(agency));
    }

    @Override
    public AgencyResponseDTO updateAgency(Long id, AgencyRequestDTO dto) {
        Agency agency = getAgency(id);
        agencyMapper.updateEntity(agency, dto);
        return agencyMapper.toDto(agencyRepository.save(agency));
    }

    @Override
    public void deleteAgency(Long id) {
        Agency agency = getAgency(id);
        if (!agency.getVehicles().isEmpty()) {
            throw new BusinessException("Impossible de supprimer une agence contenant des vehicules");
        }
        agencyRepository.delete(agency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> searchVehicles(VehicleStatus status, VehicleType type, Long agencyId, String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> status == null || vehicle.getStatut() == status)
                .filter(vehicle -> type == null || vehicle.getType() == type)
                .filter(vehicle -> agencyId == null || vehicle.getAgency().getId().equals(agencyId))
                .filter(vehicle -> normalizedKeyword.isBlank()
                        || vehicle.getMarque().toLowerCase().contains(normalizedKeyword)
                        || vehicle.getModele().toLowerCase().contains(normalizedKeyword)
                        || vehicle.getMatricule().toLowerCase().contains(normalizedKeyword))
                .sorted(Comparator.comparing(Vehicle::getId).reversed())
                .map(vehicleMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponseDTO findVehicleById(Long id) {
        return vehicleMapper.toDto(getVehicle(id));
    }

    @Override
    public VehicleResponseDTO createVehicle(VehicleRequestDTO dto) {
        if (vehicleRepository.existsByMatriculeIgnoreCase(dto.matricule())) {
            throw new BusinessException("Un vehicule avec ce matricule existe deja");
        }
        Agency agency = getAgency(dto.agencyId());
        Vehicle vehicle = vehicleMapper.toEntity(dto, agency);
        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO dto) {
        Vehicle vehicle = getVehicle(id);
        vehicleRepository.findByMatriculeIgnoreCase(dto.matricule()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BusinessException("Un autre vehicule utilise deja ce matricule");
            }
        });
        Agency agency = getAgency(dto.agencyId());
        vehicleMapper.updateEntity(vehicle, dto, agency);
        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicle(id);
        boolean hasActiveRental = vehicle.getRentals().stream().anyMatch(r -> r.getStatus() == RentalStatus.ACTIVE);
        if (hasActiveRental) {
            throw new BusinessException("Impossible de supprimer un vehicule avec une location active");
        }
        vehicleRepository.delete(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalResponseDTO> findAllRentals() {
        return rentalRepository.findAllDetailed().stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RentalResponseDTO findRentalById(Long id) {
        return rentalMapper.toDto(getRental(id));
    }

    @Override
    public RentalResponseDTO createRental(RentalRequestDTO dto) {
        if (dto.dateFin().isBefore(dto.dateDebut())) {
            throw new BusinessException("La date de fin doit etre posterieure ou egale a la date de debut");
        }
        Vehicle vehicle = getVehicle(dto.vehicleId());
        if (vehicle.getStatut() != VehicleStatus.DISPONIBLE) {
            throw new BusinessException("Ce vehicule n'est pas disponible pour la location");
        }
        long days = Math.max(1, ChronoUnit.DAYS.between(dto.dateDebut(), dto.dateFin()) + 1);
        BigDecimal totalPrice = vehicle.getPrixParJour().multiply(BigDecimal.valueOf(days));

        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setClientName(dto.clientName());
        rental.setClientEmail(dto.clientEmail());
        rental.setDateDebut(dto.dateDebut());
        rental.setDateFin(dto.dateFin());
        rental.setTotalPrice(totalPrice);
        rental.setStatus(RentalStatus.ACTIVE);
        vehicle.setStatut(VehicleStatus.LOUE);

        Rental saved = rentalRepository.save(rental);
        vehicleRepository.save(vehicle);
        return rentalMapper.toDto(saved);
    }

    @Override
    public RentalResponseDTO finishRental(Long id) {
        Rental rental = getRental(id);
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new BusinessException("Seule une location active peut etre terminee");
        }
        rental.setStatus(RentalStatus.TERMINEE);
        rental.getVehicle().setStatut(VehicleStatus.DISPONIBLE);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    public RentalResponseDTO cancelRental(Long id) {
        Rental rental = getRental(id);
        if (rental.getStatus() == RentalStatus.TERMINEE) {
            throw new BusinessException("Une location terminee ne peut pas etre annulee");
        }
        rental.setStatus(RentalStatus.ANNULEE);
        rental.getVehicle().setStatut(VehicleStatus.DISPONIBLE);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        return new DashboardStatsDTO(
                agencyRepository.count(),
                vehicleRepository.count(),
                vehicleRepository.countByStatut(VehicleStatus.DISPONIBLE),
                vehicleRepository.countByStatut(VehicleStatus.LOUE),
                vehicleRepository.countByStatut(VehicleStatus.EN_MAINTENANCE),
                rentalRepository.count(),
                rentalRepository.countByStatus(RentalStatus.ACTIVE),
                vehicleRepository.countCars(),
                vehicleRepository.countMotorcycles()
        );
    }

    private Agency getAgency(Long id) {
        return agencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable: " + id));
    }

    private Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule introuvable: " + id));
    }

    private Rental getRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location introuvable: " + id));
    }
}
