package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.model.Person;
import com.proyIntUdeA.proyectoIntegradorI.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {
    Subject saveSubject(Subject subject);

    List<Subject> getAllSubject();

    Subject getSubjectById(long id);

    boolean deleteSubject(long id);

    Subject updateSubject(long id, Subject subject);

}
