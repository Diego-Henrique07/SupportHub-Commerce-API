package supporthub_commerce_api.auth.service;
import supporthub_commerce_api.auth.dto.user.*;
import supporthub_commerce_api.exception.*;
import supporthub_commerce_api.security.JwtService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import supporthub_commerce_api.user.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import supporthub_commerce_api.user.repository.UserRepository;
import supporthub_commerce_api.user.enumsUser.UserRole;
import java.time.LocalDateTime;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("An account with this email already exists.");
        }

        User user = new User();
        String encryptedPassword = passwordEncoder.encode(request.password());

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encryptedPassword);
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new NotFoundException("email not found"));

        String token = jwtService.generationToken(new HashMap<>(), user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}

