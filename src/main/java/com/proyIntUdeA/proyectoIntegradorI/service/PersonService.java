package com.proyIntUdeA.proyectoIntegradorI.service;

import java.util.List;

import com.proyIntUdeA.proyectoIntegradorI.model.Person;

public interface PersonService {

    Person savePerson(Person person);

    List<Person> getAllPersons();

    Person getPersonById(String id);

    boolean deletePerson(String id);

    Person updatePerson(String id, Person person);
    List<Person> getAllTutors();
    boolean emailExists(String email);
}
