package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.DegreeEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;
import com.proyIntUdeA.proyectoIntegradorI.model.Degree;
import com.proyIntUdeA.proyectoIntegradorI.repository.ActivationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivationServiceImplementation implements ActivationService {
    private final ActivationRepository activationRepository;

    public ActivationServiceImplementation(ActivationRepository activationRepository) {
        this.activationRepository = activationRepository;
    }
    @Override
    public ApplicationActivationTutor saveApplication(ApplicationActivationTutor applicationActivationTutor) {
        ApplicationActivationTutorEntity applicationEntity = new ApplicationActivationTutorEntity();
        BeanUtils.copyProperties(applicationActivationTutor, applicationEntity);
        activationRepository.save(applicationEntity);
        return applicationActivationTutor;
    }

    @Override
    public List<ApplicationActivationTutor> getAllApplications() {
        List<ApplicationActivationTutorEntity> applicationEntities = activationRepository.findAll();

        return applicationEntities.stream().map(appEntity -> new ApplicationActivationTutor(
                appEntity.getApplication_tutor_id(),
                appEntity.getUser_id(),
                appEntity.getApplication_state(),
                appEntity.getApplication_date())).collect(Collectors.toList());
    }

    @Override
    public ApplicationActivationTutor getApplicationById(String id) {
        return null;
    }

    @Override
    public ApplicationActivationTutor updateApplication(ApplicationActivationTutor applicationActivationTutor) {
        return null;
    }

    @Override
    public ApplicationActivationTutor deleteApplication(String id) {
        return null;
    }

    @Override
    public ApplicationActivationTutor acceptRequest(String id) {
        return null;
    }

    @Override
    public ApplicationActivationTutor rejectRequest(String id) {
        return null;
    }
}
