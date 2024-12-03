package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;

import java.util.List;

public interface ActivationService {
    ApplicationActivationTutor saveApplication(ApplicationActivationTutor applicationActivationTutor);
    List<ApplicationActivationTutor> getAllApplications();
    ApplicationActivationTutor getApplicationById(String id);
    ApplicationActivationTutor updateApplication(ApplicationActivationTutor applicationActivationTutor);
    ApplicationActivationTutor deleteApplication(String id);
    ApplicationActivationTutor acceptRequest(String id);
    ApplicationActivationTutor rejectRequest(String id);
}
