package ma.enset.mustaphaaarab.examjee.mappers;

import ma.enset.mustaphaaarab.examjee.dtos.AgencyRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.AgencyResponseDTO;
import ma.enset.mustaphaaarab.examjee.entities.Agency;
import org.springframework.stereotype.Component;

@Component
public class AgencyMapper {
    public AgencyResponseDTO toDto(Agency agency) {
        return new AgencyResponseDTO(
                agency.getId(),
                agency.getNom(),
                agency.getAdresse(),
                agency.getVille(),
                agency.getTelephone(),
                agency.getVehicles() == null ? 0 : agency.getVehicles().size()
        );
    }

    public Agency toEntity(AgencyRequestDTO dto) {
        Agency agency = new Agency();
        updateEntity(agency, dto);
        return agency;
    }

    public void updateEntity(Agency agency, AgencyRequestDTO dto) {
        agency.setNom(dto.nom());
        agency.setAdresse(dto.adresse());
        agency.setVille(dto.ville());
        agency.setTelephone(dto.telephone());
    }
}
