package com.proyIntUdeA.proyectoIntegradorI.service;

import com.proyIntUdeA.proyectoIntegradorI.dto.BasicTutoringInfoDTO;
import com.proyIntUdeA.proyectoIntegradorI.dto.TutorActivationRequest;
import com.proyIntUdeA.proyectoIntegradorI.entity.ApplicationActivationTutorEntity;
import com.proyIntUdeA.proyectoIntegradorI.entity.DegreeEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ApplicationActivationTutor;
import com.proyIntUdeA.proyectoIntegradorI.model.Degree;
import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import com.proyIntUdeA.proyectoIntegradorI.repository.ActivationRepository;
import com.proyIntUdeA.proyectoIntegradorI.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivationServiceImplementation implements ActivationService {
    private final ActivationRepository activationRepository;
    private final DateUtils dateUtils;

    public ActivationServiceImplementation(ActivationRepository activationRepository, DateUtils dateUtils) {
        this.activationRepository = activationRepository;
        this.dateUtils = dateUtils;
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
                appEntity.getApplicationTutorId(),
                appEntity.getUserId(),
                appEntity.getUserSemester(),
                appEntity.getApplicationState(),
                appEntity.getApplicationDate())).collect(Collectors.toList());
    }

    @Override
    public Optional<ApplicationActivationTutorEntity> getApplicationById(Long id) {
        return activationRepository.findById(id);
    }

    @Override
    public ApplicationActivationTutor updateApplication(ApplicationActivationTutor applicationActivationTutor) {
        return null;
    }

    @Override
    public ApplicationActivationTutor deleteApplication(Long id) {
        return null;
    }

    @Override
    public Optional<ApplicationActivationTutorEntity> acceptRequest(Long id) {
        Optional<ApplicationActivationTutorEntity> app = activationRepository.findById(id);
        app.ifPresent(applicationActivationTutorEntity -> applicationActivationTutorEntity.setApplicationState("aceptada"));
        activationRepository.save(app.get());
        return app;
    }

    @Override
    public Optional<ApplicationActivationTutorEntity> rejectRequest(Long id) {
        Optional<ApplicationActivationTutorEntity> app = activationRepository.findById(id);
        app.ifPresent(applicationActivationTutorEntity -> applicationActivationTutorEntity.setApplicationState("rechazada"));
        activationRepository.save(app.get());
        return app;
    }

    @Override
    public List<TutorActivationRequest> listActivations() {
        List<Object[]> rawData = activationRepository.findAllApplications();

        return rawData.stream().map(row -> {
            String fileId = row[5].toString();

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/fileManager/files/")
                    .path(fileId)
                    .toUriString();

            return new TutorActivationRequest(
                    dateUtils.formatearfecha((Date) row[0]),
                    (String) row[1],
                    row[2].toString(),
                    (String) row[3],
                    (String) row[4],
                    fileDownloadUri
            );
        }).collect(Collectors.toList());
    }


}
