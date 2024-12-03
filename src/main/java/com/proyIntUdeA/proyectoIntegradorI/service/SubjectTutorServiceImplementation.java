package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.UserXSubjectEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.UserXSubject;
import com.proyIntUdeA.proyectoIntegradorI.repository.SubjectTutorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectTutorServiceImplementation implements SubjectTutorService {

    private final SubjectTutorRepository subjectTutorRepository;

    public SubjectTutorServiceImplementation(SubjectTutorRepository subjectTutorRepository) {
        this.subjectTutorRepository = subjectTutorRepository;
    }
    @Override
    public UserXSubject saveSubjectTutor(UserXSubject userXSubject) {
        UserXSubjectEntity userXSubjectEntity = new UserXSubjectEntity();
        BeanUtils.copyProperties(userXSubject, userXSubjectEntity);
        subjectTutorRepository.save(userXSubjectEntity);
        return userXSubject;
    }

    @Override
    public UserXSubject getSubjectTutorById(Long id) {
        return null;
    }

    @Override
    public List<UserXSubjectEntity> getAllSubjectTutor() {
        List<UserXSubjectEntity> subjectTutorEntities = subjectTutorRepository.findAll();

        return subjectTutorEntities.stream().map(userXSubjectEntity -> new UserXSubjectEntity(
                userXSubjectEntity.getSubject_tutor_id(),
                userXSubjectEntity.getUser_id(),
                userXSubjectEntity.getSubject_id())).collect(Collectors.toList());
    }

    @Override
    public boolean deleteSubjectTutorById(Long id) {
        return false;
    }
}
