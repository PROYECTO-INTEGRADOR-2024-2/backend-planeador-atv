package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorAvailabilityRequestDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.TutorSearchDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SlotAvailabilityEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.UserXSubjectEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.UserXSubject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectTutorService {
    UserXSubject saveSubjectTutor(UserXSubject userXSubject);
    UserXSubject getSubjectTutorById(Long id);
    List<UserXSubjectEntity> getAllSubjectTutor();
    boolean deleteSubjectTutorById(Long id);
    List<UserXSubject> saveSubjectTutorList(String user_id, List<Long> subjectIds);
    UserXSubjectEntity getUserXSubjectOrThrow(String user_id, Long subject_id);

    List<SlotAvailabilityEntity> saveTutorAvailability(TutorAvailabilityRequestDTO request);

    List<SlotAvailabilityEntity> getTutorAvailability(String tutorId);

    List<TutorSearchDTO> findByTime(Long subjectId,String hour,String day, String period);

}
