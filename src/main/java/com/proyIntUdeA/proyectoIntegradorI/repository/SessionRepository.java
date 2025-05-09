package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    //Consulta para traer la información de la tabla de tutorías que pedí como estudiante
    @Query(value = "SELECT c.class_id, c.class_date, s.subject_name, c.registered, c.canceled_by, c.class_topics, t.user_id, t.user_firstname, t.user_lastname " +
            "FROM class c " +
            "JOIN subject s ON s.subject_id = c.subject_id " +
            "JOIN person t ON t.user_id = c.tutor_id " +
            "WHERE c.student_id = :studentId", nativeQuery = true)
    List<Object[]> findBasicTutoInfoRaw(@Param("studentId") String studentId);

    @Query(value = "SELECT c.class_id, c.class_date, s.subject_name, c.registered, c.canceled_by, c.class_topics, e.user_id, e.user_firstname, e.user_lastname " +
            "FROM class c " +
            "JOIN subject s ON s.subject_id = c.subject_id " +
            "JOIN person e ON e.user_id = c.student_id " +
            "WHERE c.tutor_id = :tutorId", nativeQuery = true)
    List<Object[]> findBasicTutoInfoTutorRaw(@Param("tutorId") String tutorId);





}
