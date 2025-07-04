package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivationRepository extends JpaRepository<ApplicationActivationTutorEntity, Long> {
    @Query(value = "SELECT a.application_date, a.application_state, a.user_id, p.user_firstname, a.user_semester, f.id " +
            "FROM application_activation_tutor_entity a " +
            "JOIN person p ON p.user_id = a.user_id " +
            "JOIN files f on f.user_id = a.user_id", nativeQuery = true)
    List<Object[]> findAllApplications();

}
