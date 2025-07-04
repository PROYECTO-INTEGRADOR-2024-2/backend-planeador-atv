package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorActivationRequest;
import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;

import java.util.List;
import java.util.Optional;

public interface ActivationService {
    ApplicationActivationTutor saveApplication(ApplicationActivationTutor applicationActivationTutor);
    List<ApplicationActivationTutor> getAllApplications();
    Optional<ApplicationActivationTutorEntity> getApplicationById(Long id);
    ApplicationActivationTutor updateApplication(ApplicationActivationTutor applicationActivationTutor);
    ApplicationActivationTutor deleteApplication(Long id);
    Optional<ApplicationActivationTutorEntity> acceptRequest(Long id);
    Optional<ApplicationActivationTutorEntity> rejectRequest(Long id);
    List<TutorActivationRequest> listActivations();
}
