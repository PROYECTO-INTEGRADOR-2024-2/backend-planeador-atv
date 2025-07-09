package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorSearchDTO;
import com.proyIntUdeA.proyectoIntegradorI.model.ListTutorsByTimeRequest;
import com.proyIntUdeA.proyectoIntegradorI.service.SessionService;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectTutorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyIntUdeA.proyectoIntegradorI.Jwt.JwtService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
public class AvailabilityController {
    private final SubjectTutorService subjectTutorService;
    private final SessionService sessionService;
    private final JwtService jwtService;

    public AvailabilityController(SubjectTutorService subjectTutorService, SessionService sessionService, JwtService jwtService) {
        this.subjectTutorService = subjectTutorService;
        this.sessionService = sessionService;
        this.jwtService = jwtService;
    }

    @PostMapping("/listarTutores")
    public ResponseEntity<?> findTutorsByTime(@RequestBody ListTutorsByTimeRequest listTutorsByTimeRequest, HttpServletRequest request) {
        ResponseEntity<String> tokenVerification = jwtService.verifyToken(request);

        if (tokenVerification.getStatusCode() != HttpStatus.OK) {
            return tokenVerification;
        }

        String token = request.getHeader("Authorization").substring(7);
        String tutorId = jwtService.getClaim(token, claims -> claims.get("user_id", String.class));


        Long subjectId = listTutorsByTimeRequest.getSubjectId();
        String hour = listTutorsByTimeRequest.getHour();
        String day = listTutorsByTimeRequest.getDayWeek();
        String period = listTutorsByTimeRequest.getPeriod();
        String date = listTutorsByTimeRequest.getDate();

        List<TutorSearchDTO> tutors = subjectTutorService.findByTime(subjectId, hour, day, period).stream()
                .filter(tutor -> !sessionService.verificarDispoTutor(tutor.getTutorId(), date, hour, period))
                .filter(tutor -> !tutor.getTutorId().equals(tutorId))
                .collect(Collectors.toList());

        // Crear y agregar el tutor por defecto con ID "0"
        TutorSearchDTO defaultTutor = new TutorSearchDTO("0", "Cualquier", "Tutor");

        // Verificar si ya existe un tutor con ID "0" en la lista
        boolean alreadyIncluded = tutors.stream()
                .anyMatch(t -> "0".equals(t.getTutorId()));

        if (!alreadyIncluded) {
            tutors.add(defaultTutor);
        }



        return ResponseEntity.ok(tutors);
    }



}
