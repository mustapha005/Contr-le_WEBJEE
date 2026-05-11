package ma.enset.mustaphaaarab.examjee.services;

import ma.enset.mustaphaaarab.examjee.dtos.AuthRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.AuthResponseDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO authenticate(AuthRequestDTO dto);
    AuthResponseDTO register(RegisterRequestDTO dto);
}
