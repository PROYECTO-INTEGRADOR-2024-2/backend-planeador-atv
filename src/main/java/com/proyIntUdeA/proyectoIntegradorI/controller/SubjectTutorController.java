package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.entity.UserXSubjectEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.UserXSubject;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectTutorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/")
public class SubjectTutorController {
    private final SubjectTutorService subjectTutorService;

    public SubjectTutorController(SubjectTutorService subjectTutorService) {
        this.subjectTutorService = subjectTutorService;
    }

    @PostMapping("/subjectTutor")
    public UserXSubject saveSubjectTutor(@RequestBody UserXSubject subjectTutor) {
        return subjectTutorService.saveSubjectTutor(subjectTutor);
    }

    @GetMapping("/subjectTutor")
    public List<UserXSubjectEntity> getAllSubjectTutor() {
        return subjectTutorService.getAllSubjectTutor();
    }

}
