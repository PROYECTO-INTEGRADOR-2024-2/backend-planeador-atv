package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;
import com.proyIntUdeA.proyectoIntegradorI.service.ActivationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ApplicationController {
    private ActivationService activationService;

    public ApplicationController(ActivationService activationService) {
        this.activationService = activationService;
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
}
