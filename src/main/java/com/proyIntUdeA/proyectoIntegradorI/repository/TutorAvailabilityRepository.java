package com.proyIntUdeA.proyectoIntegradorI.repository;

import com.proyIntUdeA.proyectoIntegradorI.entity.SlotAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorAvailabilityRepository extends JpaRepository<SlotAvailabilityEntity, Long> {
    List<SlotAvailabilityEntity> findByTutorId(String tutorId);
    void deleteByTutorId(String tutorId);
}
