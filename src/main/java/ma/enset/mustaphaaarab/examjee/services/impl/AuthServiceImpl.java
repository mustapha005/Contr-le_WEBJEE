package ma.enset.mustaphaaarab.examjee.services.impl;

import ma.enset.mustaphaaarab.examjee.dtos.AuthRequestDTO;
import ma.enset.mustaphaaarab.examjee.dtos.AuthResponseDTO;
import ma.enset.mustaphaaarab.examjee.dtos.RegisterRequestDTO;
import ma.enset.mustaphaaarab.examjee.entities.AppUser;
import ma.enset.mustaphaaarab.examjee.enums.UserRole;
import ma.enset.mustaphaaarab.examjee.exceptions.BusinessException;
import ma.enset.mustaphaaarab.examjee.repositories.UserRepository;
import ma.enset.mustaphaaarab.examjee.security.JwtService;
import ma.enset.mustaphaaarab.examjee.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDTO authenticate(AuthRequestDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        AppUser user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new BusinessException("Utilisateur introuvable"));
        return toAuthResponse(user);
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new BusinessException("Ce nom d'utilisateur existe deja");
        }
        AppUser user = new AppUser();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setFullName(dto.fullName());
        user.setRole(UserRole.ROLE_CLIENT);
        userRepository.save(user);
        return toAuthResponse(user);
    }

    private AuthResponseDTO toAuthResponse(AppUser user) {
        return new AuthResponseDTO(
                jwtService.generateToken(user),
                "Bearer",
                user.getUsername(),
                user.getFullName(),
                user.getRole()
        );
    }
}
