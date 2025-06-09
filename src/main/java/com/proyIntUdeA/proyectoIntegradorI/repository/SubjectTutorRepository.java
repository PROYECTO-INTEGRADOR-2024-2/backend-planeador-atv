package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.entity.UserXSubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SubjectTutorRepository extends JpaRepository<UserXSubjectEntity, Long> {
    UserXSubjectEntity findByUserIdAndSubjectId(String user_id, Long subject_id);
    Optional<UserXSubjectEntity> findOptionalByUserIdAndSubjectId(String user_id, Long subject_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE from bloque_disponibilidad WHERE tutor_id = :tutorId", nativeQuery = true)
    void eliminarBloquesXTutorId(@Param("tutorId") String tutorId);
}
