package com.proyIntUdeA.proyectoIntegradorI.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
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
        person.setUser_role("Tutor");
        person = personService.updatePerson(id, person);
        return ResponseEntity.ok(person);
    }

    @PutMapping("/persons/activateAdmin/{id}")
    public ResponseEntity<Person> activateAdmin(@PathVariable("id") String id) {
        Person person = personService.getPersonById(id);
        person.setUser_role("Admin");
        person = personService.updatePerson(id, person);
        return ResponseEntity.ok(person);
    }

    @GetMapping("/persons/tutor")
    public List<Person> getAllTutors(){
        return personService.getAllTutors();
    }
}
