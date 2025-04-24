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
        PersonEntity personEntity = new PersonEntity();
        BeanUtils.copyProperties(person, personEntity);
        personRepository.save(personEntity);
        System.out.println(person.getUserFirstname());
        System.out.println(person.getUserEmail());
        return person;
    }

    @Override
    public List<Person> getAllPersons() {
        List<PersonEntity> personEntities = personRepository.findAll();

        return personEntities.stream().map(personEntity -> new Person(
                personEntity.getUserId(),
                personEntity.getUserIdType(),
                personEntity.getUserFirstname(),
                personEntity.getUserLastname(),
                personEntity.getUserEmail(),
                personEntity.getUserPassword(),
                personEntity.getUserPhone(),
                personEntity.getUserDepartment(),
                personEntity.getUserCity(),
                personEntity.getUserState(),
                personEntity.getUserRole())).collect(Collectors.toList());
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
        personEntity.setUserCity(person.getUserCity());
        personEntity.setUserDepartment(person.getUserDepartment());
        personEntity.setUserEmail(person.getUserEmail());
        personEntity.setUserLastname(person.getUserLastname());
        personEntity.setUserFirstname(person.getUserFirstname());
        personEntity.setUserPassword(person.getUserPassword());
        personEntity.setUserPhone(person.getUserPhone());
        personEntity.setUserPhone(person.getUserPhone());
        personEntity.setUserRole(person.getUserRole());
        personEntity.setUserState(person.getUserState());

        personRepository.save(personEntity);
        return person;
    }

    public List<Person> getAllTutors() {
        List<Person> persons = getAllPersons();
        List<Person> tutors = persons
                .stream()
                .filter(person -> person.getUserRole().equals("Tutor"))
                .collect(Collectors.toList());
        return tutors;
    }

    public boolean emailExists(String email) {
        List<PersonEntity> persons = personRepository.findAll();
        for (PersonEntity person : persons) {
            if (ObjectUtils.equals(person.getUserEmail(), email)) {
                return true;
            }
        }
        return false;
    }
}
