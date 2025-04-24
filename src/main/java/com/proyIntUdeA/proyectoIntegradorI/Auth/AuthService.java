package com.proyIntUdeA.proyectoIntegradorI.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;
import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.repository.PersonRepository;
import lombok.RequiredArgsConstructor;

import java.sql.SQLOutput;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository personRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUser_email(), request.getUser_password()));
        UserDetails user = personRepository.findByUserEmail(request.getUser_email()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse register(RegisterRequest request) {
        PersonEntity person = PersonEntity.builder()
                .userFirstname(request.getUser_firstname())
                .userId(request.getUser_id())
                .userIdType(request.getUser_id_type())
                .userLastname(request.getUser_lastname())
                .userEmail(request.getUserEmail())
                .userPassword(passwordEncoder.encode(request.getUser_password()))
                .userPhone(request.getUser_phone())
                .userDepartment(request.getUser_department())
                .userCity(request.getUser_city())
                .userState(request.getUser_state())
                .userRole(request.getUser_role())
                .build();

        personRepository.save(person);

        return AuthResponse.builder()
                .token(jwtService.getToken(person))
                .build();
    }

}
