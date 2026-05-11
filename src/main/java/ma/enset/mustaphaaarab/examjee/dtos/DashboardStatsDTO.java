package ma.enset.mustaphaaarab.examjee.dtos;

public record DashboardStatsDTO(
        long totalAgencies,
        long totalVehicles,
        long availableVehicles,
        long rentedVehicles,
        long maintenanceVehicles,
        long totalRentals,
        long activeRentals,
        long cars,
        long motorcycles
) {
}
