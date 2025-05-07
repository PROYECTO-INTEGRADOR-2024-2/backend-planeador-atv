package com.proyIntUdeA.proyectoIntegradorI.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PersonController {
    @Autowired
    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

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

    @PutMapping("/persons/activateTutor/{id}")
    public ResponseEntity<Person> activateTutor(@PathVariable("id") String id) {
        Person person = personService.getPersonById(id);
        person.setUserRole("Tutor");
        person = personService.updatePerson(id, person);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/persons/activateAdmin/{id}")
    public ResponseEntity<Person> activateAdmin(@PathVariable("id") String id) {
        Person person = personService.getPersonById(id);
        person.setUserRole("Admin");
        person = personService.updatePerson(id, person);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/persons/disableUser/{id}")
    public ResponseEntity<Person> disableUser(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or malformed Authorization header");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token missing or invalid");
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey("586E3272357538782F413F4428472B4B6250655368566B597033733676397924") // Tu SECRET_KEY
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("Token parsed successfully");
            System.out.println("Claims: " + claims);
        } catch (Exception e) {
            System.out.println("Token parsing failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        String role = (String) claims.get("user_role");
        System.out.println("Role from token: " + role);

        if (role == null || !role.equalsIgnoreCase("admin")) {
            System.out.println("Access denied: user_role is not admin");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin can disable users");
        }

        System.out.println("User authorized as admin. Disabling user with ID: " + id);

        Person person = personService.getPersonById(id);
        person.setUserState("0");
        person = personService.updatePerson(id, person);

        System.out.println("User successfully disabled");
        return ResponseEntity.ok(person);
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
