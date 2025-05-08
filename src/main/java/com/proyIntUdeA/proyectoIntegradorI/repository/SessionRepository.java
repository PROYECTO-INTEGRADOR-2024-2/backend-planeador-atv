package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    @Query(value = "SELECT c.class_date, s.subject_name, c.class_state, t.user_id, t.user_firstname, t.user_lastname " +
            "FROM class c " +
            "JOIN subject s ON s.subject_id = c.subject_id " +
            "JOIN person t ON t.user_id = c.tutor_id " +
            "WHERE c.student_id = :studentId", nativeQuery = true)
    List<Object[]> findBasicTutoInfoRaw(@Param("studentId") String studentId);

}
