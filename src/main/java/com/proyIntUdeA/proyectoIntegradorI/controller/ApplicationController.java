package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;
import com.proyIntUdeA.proyectoIntegradorI.service.ActivationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
