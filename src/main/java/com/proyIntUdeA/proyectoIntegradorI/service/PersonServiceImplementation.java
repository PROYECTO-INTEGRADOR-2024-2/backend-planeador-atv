package com.proyIntUdeA.proyectoIntegradorI.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.Person;
import com.proyIntUdeA.proyectoIntegradorI.repository.PersonRepository;

@Service
public class PersonServiceImplementation implements PersonService {

    private PersonRepository personRepository;

    public PersonServiceImplementation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person savePerson(Person person) {
        if(personRepository.findById(person.getUser_id()) == null){
            return null;
        }
        PersonEntity personEntity = new PersonEntity();
        BeanUtils.copyProperties(person, personEntity);
        personRepository.save(personEntity);
        System.out.println(person.getUser_firstname());
        System.out.println(person.getUserEmail());
        return person;
    }

    @Override
    public List<Person> getAllPersons() {
        List<PersonEntity> personEntities = personRepository.findAll();

        return personEntities.stream().map(personEntity -> new Person(
                personEntity.getUser_id(),
                personEntity.getUser_id_type(),
                personEntity.getUser_firstname(),
                personEntity.getUser_lastname(),
                personEntity.getUserEmail(),
                personEntity.getUser_password(),
                personEntity.getUser_phone(),
                personEntity.getUser_department(),
                personEntity.getUser_city(),
                personEntity.getUser_state(),
                personEntity.getUser_role())).collect(Collectors.toList());
    }

    @Override
    public Person getPersonById(String id) {
        PersonEntity personEntity = personRepository.findById(id).get();
        Person person = new Person();
        BeanUtils.copyProperties(personEntity, person);
        return person;
    }

    @Override
    public boolean deletePerson(String id) {
        PersonEntity person = personRepository.findById(id).get();
        personRepository.delete(person);
        return true;
    }

    @Override
    public Person updatePerson(String id, Person person) {
        PersonEntity personEntity = personRepository.findById(id).get();
        personEntity.setUser_city(person.getUser_city());
        personEntity.setUser_department(person.getUser_department());
        personEntity.setUserEmail(person.getUserEmail());
        personEntity.setUser_lastname(person.getUser_lastname());
        personEntity.setUser_firstname(person.getUser_firstname());
        personEntity.setUser_password(person.getUser_password());
        personEntity.setUser_phone(person.getUser_phone());
        personEntity.setUser_phone(person.getUser_phone());
        personEntity.setUser_role(person.getUser_role());
        personEntity.setUser_state(person.getUser_state());

        personRepository.save(personEntity);
        return person;
    }

    public List<Person> getAllTutors() {
        List<Person> persons = getAllPersons();
        List<Person> tutors = persons
                .stream()
                .filter(person -> person.getUser_role().equals("Tutor"))
                .collect(Collectors.toList());
        return tutors;
    }
}
