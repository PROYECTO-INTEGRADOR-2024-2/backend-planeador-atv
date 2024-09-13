package com.proyIntUdeA.proyectoIntegradorI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyIntUdeA.proyectoIntegradorI.entity.PersonEntity;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, String>{

}