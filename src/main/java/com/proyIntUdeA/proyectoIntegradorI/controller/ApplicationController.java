package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;
import com.proyIntUdeA.proyectoIntegradorI.model.UserSubjectRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.UserXSubject;
import com.proyIntUdeA.proyectoIntegradorI.service.ActivationService;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectTutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ApplicationController {
    private ActivationService activationService;
    private SubjectTutorService subjectTutorService;
    
    public ApplicationController(ActivationService activationService, SubjectTutorService subjectTutorService) {
        this.activationService = activationService;
        this.subjectTutorService = subjectTutorService;
    }

    @PostMapping("/application")
    public ApplicationActivationTutor createApplication(@RequestBody ApplicationActivationTutor applicationActivationTutor) {
        return activationService.saveApplication(applicationActivationTutor);
    }

    @GetMapping("/application")
    public List<ApplicationActivationTutor> getAllApplications() {
        return activationService.getAllApplications();
    }

    @PutMapping("/application/accept/{id}")
    public Optional<ApplicationActivationTutorEntity> acceptApplication(@PathVariable Long id) {
        return activationService.acceptRequest(id);
    }

    @PutMapping("/application/reject/{id}")
    public Optional<ApplicationActivationTutorEntity> rejectApplication(@PathVariable Long id) {
        return activationService.rejectRequest(id);
    }

    @PostMapping("/application/subjects")
    public ResponseEntity<List<UserXSubject>> addUserSubject(@RequestBody UserSubjectRequest userSubjectRequest){
        List<UserXSubject> saved = subjectTutorService.saveSubjectTutorList(
                        userSubjectRequest.getUser_id(),
                        userSubjectRequest.getSubject_ids()
                );
                return ResponseEntity.ok(saved);
    }
}
