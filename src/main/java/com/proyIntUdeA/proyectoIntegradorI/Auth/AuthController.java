package com.proyIntUdeA.proyectoIntegradorI.Auth;

import com.proyIntUdeA.proyectoIntegradorI.model.Person;
import com.proyIntUdeA.proyectoIntegradorI.service.PersonService;

import java.util.List;

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

    private final AuthService authService;
    private final PersonService personService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        List<Person> personas = personService.getAllPersons();

        for (Person person : personas) {
            if (request.getUser_id().equals(person.getUser_id())
                    || request.getUserEmail().equals(person.getUserEmail())) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.ok(authService.register(request));
    }
}
