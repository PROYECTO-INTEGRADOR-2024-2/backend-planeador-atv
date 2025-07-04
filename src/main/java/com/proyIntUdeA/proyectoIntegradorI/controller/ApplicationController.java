package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorActivationRequest;
import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;
import com.proyIntUdeA.proyectoIntegradorI.model.UserSubjectRequest;
import com.proyIntUdeA.proyectoIntegradorI.model.UserXSubject;
import com.proyIntUdeA.proyectoIntegradorI.service.ActivationService;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectTutorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<TutorActivationRequest> getAllApplications() {
        return activationService.listActivations();
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
    public ResponseEntity<List<UserXSubject>> addUserSubject(@RequestBody UserSubjectRequest userSubjectRequest) {
        List<Long> newSubjectIds = userSubjectRequest.getSubject_ids().stream()
                .filter(subjectId -> {
                    try {
                        subjectTutorService.getUserXSubjectOrThrow(userSubjectRequest.getUser_id(), subjectId);
                        return false;
                    } catch (EntityNotFoundException e) {
                        return true;
                    }
                })
                .collect(Collectors.toList());
        if (newSubjectIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<UserXSubject> saved = subjectTutorService.saveSubjectTutorList(
                userSubjectRequest.getUser_id(),
                newSubjectIds
        );

        return ResponseEntity.ok(saved);
    }
}