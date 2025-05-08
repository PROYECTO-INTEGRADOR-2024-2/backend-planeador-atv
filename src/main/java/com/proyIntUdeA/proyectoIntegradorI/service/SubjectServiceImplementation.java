package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.SubjectEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.Subject;
import com.proyIntUdeA.proyectoIntegradorI.repository.SubjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImplementation implements SubjectService{
    private final SubjectRepository subjectRepository;

    public SubjectServiceImplementation(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }


    @Override
    public Subject saveSubject(Subject subject) {
        SubjectEntity subjectEntity = new SubjectEntity();
        BeanUtils.copyProperties(subject, subjectEntity);
        subjectRepository.save(subjectEntity);
        return subject;
    }

    @Override
    public List<Subject> getAllSubject() {
        List<SubjectEntity> subjectEntities = subjectRepository.findAll();

        return subjectEntities.stream().map(subjectEntity -> new Subject(
                subjectEntity.getSubjectId(),
                subjectEntity.getDegreeId(),
                subjectEntity.getSubjectName())).collect(Collectors.toList());
    }

    @Override
    public Subject getSubjectById(long id) {
        SubjectEntity subjectEntity = subjectRepository.findById(id).get();
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectEntity, subject);
        return subject;
    }

    @Override
    public boolean deleteSubject(long id) {
        SubjectEntity subject = subjectRepository.findById(id).get();
        subjectRepository. delete(subject);
        return true;
    }

    @Override
    public Subject updateSubject(long id, Subject subject) {
        SubjectEntity subjectEntity = subjectRepository.findById(id).get();
        subjectEntity.setSubjectName(subject.getSubjectName());

        subjectRepository.save(subjectEntity);
        return subject;
    }

}
