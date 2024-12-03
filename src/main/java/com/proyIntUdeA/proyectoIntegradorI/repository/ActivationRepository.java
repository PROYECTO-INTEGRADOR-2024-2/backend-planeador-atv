package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationRepository extends JpaRepository<ApplicationActivationTutorEntity, Long> {

}
