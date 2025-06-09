package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorSearchDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SlotAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorAvailabilityRepository extends JpaRepository<SlotAvailabilityEntity, Long> {
    List<SlotAvailabilityEntity> findByTutorId(String tutorId);
    void deleteByTutorId(String tutorId);

    @Query(value = "select DISTINCT " +
            "av.tutor_id, " +
            "tu.user_firstname, " +
            "tu.user_lastname " +
            "from bloque_disponibilidad av " +
            "join person tu on tu.user_id = av.tutor_id " +
            "join userxsubject_entity us ON us.user_id = av.tutor_id " +
            "where av.day = :day " +
            "and av.start_time = :hour " +
            "and us.subject_id = :subjectId", nativeQuery = true)
    List<Object[]> findByTime (
            @Param ("subjectId") Long subjectId,
            @Param ("hour") String hour,
            @Param ("day") String day
    );
}
