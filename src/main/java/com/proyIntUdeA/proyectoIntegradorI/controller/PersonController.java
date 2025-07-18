package com.proyIntUdeA.proyectoIntegradorI.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;
import com.proyIntUdeA.proyectoIntegradorI.service.ActivationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyIntUdeA.proyectoIntegradorI.model.Person;
import com.proyIntUdeA.proyectoIntegradorI.service.PersonService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final JwtService jwtService;
    private final ActivationService activationService;

    @PostMapping("/persons")
    public Person savePerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") String id) {
        Person person = null;
        person = personService.getPersonById(id);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("persons/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePerson(@PathVariable("id") String id) {
        boolean deleted = false;
        deleted = personService.deletePerson(id);
        Map<String, Boolean> response= new HashMap<>();
        response.put("deleted", deleted);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id") String id, @RequestBody Person person) {
        person = personService.updatePerson(id, person);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/persons/activateTutor/{user_id}/{app_id}")
    public ResponseEntity<?> activateTutor(@PathVariable("user_id") String user_id, @PathVariable("app_id") Long app_id, HttpServletRequest request) {
        ResponseEntity<String> tokenVerification = jwtService.verifyToken(request);

        if (tokenVerification.getStatusCode() != HttpStatus.OK) {
            return tokenVerification;
        }

        String token = request.getHeader("Authorization").substring(7);
        String user_role = jwtService.getClaim(token, claims -> claims.get("user_role", String.class)).toLowerCase();
        if(!user_role.equalsIgnoreCase("ROLE_ADMIN")){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Debes ser usuario administrador");
        }

        Person person = personService.getPersonById(user_id);
        if(person.getUserRole().equalsIgnoreCase("ROLE_STUDENT")){
            person.setUserRole("ROLE_TUTOR");
            person = personService.updatePerson(user_id, person);
            activationService.acceptRequest(app_id);
            return ResponseEntity.ok(person);
        }else{
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("El usuario al que se desea activar ya tiene rol de tutor o administrador. No se realiza ninguna acción");
        }

    }

    @PutMapping("/persons/activateAdmin/{id}")
    public ResponseEntity<Person> activateAdmin(@PathVariable("id") String id) {
        Person person = personService.getPersonById(id);
        person.setUserRole("Admin");
        person = personService.updatePerson(id, person);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/persons/disableUser/{id}")
    public ResponseEntity<Map<String, String>> disableUser(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token missing or invalid"));
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("586E3272357538782F413F4428472B4B6250655368566B597033733676397924")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid token"));
        }

        String role = (String) claims.get("user_role");
        if (role == null || !role.equalsIgnoreCase("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admin can disable users"));
        }

        try {
            Person person = personService.getPersonById(id);
            person.setUserState("0");
            personService.updatePerson(id, person);
            return ResponseEntity
                    .ok(Map.of("message", "User disabled successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error disabling user"));
        }
    }


    @PutMapping("/persons/enableUser/{id}")
    public ResponseEntity<Map<String, String>> enableUser(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token missing or invalid"));
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("586E3272357538782F413F4428472B4B6250655368566B597033733676397924")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid token"));
        }

        String role = (String) claims.get("user_role");
        if (role == null || !role.equalsIgnoreCase("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admin can enable users"));
        }

        try {
            Person person = personService.getPersonById(id);
            person.setUserState("1");
            personService.updatePerson(id, person);
            return ResponseEntity
                    .ok(Map.of("message", "User enabled successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error enabling user"));
        }
    }


    @GetMapping("/persons/tutor")
    public List<Person> getAllTutors(){
        return personService.getAllTutors();
    }

    @GetMapping("/persons/email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable("email") String email) {
        boolean exists = personService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
}
