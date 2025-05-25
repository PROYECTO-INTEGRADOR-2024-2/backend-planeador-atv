package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorAvailabilityRequestDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SlotAvailabilityEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.UserXSubjectEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.UserXSubject;
import com.proyIntUdeA.proyectoIntegradorI.repository.SubjectTutorRepository;
import com.proyIntUdeA.proyectoIntegradorI.repository.TutorAvailabilityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectTutorServiceImplementation implements SubjectTutorService {

    private final SubjectTutorRepository subjectTutorRepository;
    private final TutorAvailabilityRepository availabilityRepository;

    public SubjectTutorServiceImplementation(SubjectTutorRepository subjectTutorRepository, TutorAvailabilityRepository availabilityRepository) {
        this.subjectTutorRepository = subjectTutorRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public List<UserXSubject> saveSubjectTutorList(String userId, List<Long> subjectIds){
        List<UserXSubjectEntity> entities = subjectIds.stream()
                .map(subjectId -> {
                    UserXSubjectEntity entity = new UserXSubjectEntity();
                    entity.setSubjectId(subjectId);
                    entity.setUserId(userId);
                    return entity;
                }).collect(Collectors.toList());

        subjectTutorRepository.saveAll(entities);

        return entities.stream().map(entity -> {
            UserXSubject model = new UserXSubject();
            BeanUtils.copyProperties(entity, model);
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public UserXSubject saveSubjectTutor(UserXSubject userXSubject) {
        UserXSubjectEntity userXSubjectEntity = new UserXSubjectEntity();
        BeanUtils.copyProperties(userXSubject, userXSubjectEntity);
        subjectTutorRepository.save(userXSubjectEntity);
        return userXSubject;
    }

    public UserXSubjectEntity getUserXSubjectOrThrow(String userId, Long subjectId) {
        return subjectTutorRepository.findOptionalByUserIdAndSubjectId(userId, subjectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró UserXSubject con userId: " + userId + " y subjectId: " + subjectId));
    }

    @Override
    public UserXSubject getSubjectTutorById(Long id) {
        return null;
    }

    @Override
    public List<UserXSubjectEntity> getAllSubjectTutor() {
        List<UserXSubjectEntity> subjectTutorEntities = subjectTutorRepository.findAll();

        return subjectTutorEntities.stream().map(userXSubjectEntity -> new UserXSubjectEntity(
                userXSubjectEntity.getSubjectTutorId(),
                userXSubjectEntity.getUserId(),
                userXSubjectEntity.getSubjectId())).collect(Collectors.toList());
    }

    @Override
    public boolean deleteSubjectTutorById(Long id) {
        return false;
    }

    @Override
    public List<SlotAvailabilityEntity> saveTutorAvailability(TutorAvailabilityRequestDTO request){
        List<SlotAvailabilityEntity> availabilities = request.getDisponibilidad().stream()
                .map(av -> new SlotAvailabilityEntity(
                        null,
                        request.getTutorId(),
                        av.getDia(),
                        av.getHoraInicio(),
                        av.getHoraFinal(),
                        av.getPeriodo()
                ))
                .collect(Collectors.toList());
        return availabilityRepository.saveAll(availabilities);
    }

    @Override
    public List<SlotAvailabilityEntity> getTutorAvailability(String tutorId) {
        return availabilityRepository.findByTutorId(tutorId);
    }
}
