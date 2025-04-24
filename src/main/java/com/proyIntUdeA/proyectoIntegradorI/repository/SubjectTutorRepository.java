package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.entity.UserXSubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectTutorRepository extends JpaRepository<UserXSubjectEntity, Long> {
    UserXSubjectEntity findByUserIdAndSubjectId(String user_id, Long subject_id);
    Optional<UserXSubjectEntity> findOptionalByUserIdAndSubjectId(String user_id, Long subject_id);

}
