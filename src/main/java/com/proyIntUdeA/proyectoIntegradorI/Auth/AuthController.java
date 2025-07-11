package com.proyIntUdeA.proyectoIntegradorI.Auth;

import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;
import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.EmailRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.JwtResponse;
import com.proyIntUdeA.proyectoIntegradorI.model.Person;
import com.proyIntUdeA.proyectoIntegradorI.repository.PersonRepository;
import com.proyIntUdeA.proyectoIntegradorI.service.PersonService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PersonRepository personRepository;
    private final AuthService authService;
    private final PersonService personService;
    private final JwtService jwtService;
    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String email = request.getUser_email();

        Optional<PersonEntity> user = personRepository.findByUserEmail(email);

        if (!user.isPresent()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario no está registrado");
        } else if(user.get().getUserState().equals("0")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario se encuentra deshabilitado");
        }
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        List<Person> personas = personService.getAllPersons();

        for (Person person : personas) {
            if (request.getUserId().equals(person.getUserId())
                    || request.getUserEmail().equals(person.getUserEmail())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Ya existe un usuario con el mismo correo o ID registrado en la plataforma");
            }
        }

        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody EmailRequest request) {
        String email = request.getEmail();

        Optional<PersonEntity> user = personRepository.findByUserEmail(email);
        if (user.isPresent()) {
            String jwt = jwtService.getToken(user.get());
            return ResponseEntity.ok(new JwtResponse(jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not registered");
        }
    }
}
