package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorSearchDTO;
import com.proyIntUdeA.proyectoIntegradorI.model.ListTutorsByTimeRequest;
import com.proyIntUdeA.proyectoIntegradorI.service.SessionService;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectTutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
public class AvailabilityController {
    private final SubjectTutorService subjectTutorService;
    private final SessionService sessionService;

    public AvailabilityController(SubjectTutorService subjectTutorService, SessionService sessionService) {
        this.subjectTutorService = subjectTutorService;
        this.sessionService = sessionService;
    }

    @PostMapping("/listarTutores")
    public ResponseEntity<List<TutorSearchDTO>> findTutorsByTime(@RequestBody ListTutorsByTimeRequest listTutorsByTimeRequest){
        Long subjectId = listTutorsByTimeRequest.getSubjectId();
        String hour = listTutorsByTimeRequest.getHour();
        String day = listTutorsByTimeRequest.getDayWeek();
        String period = listTutorsByTimeRequest.getPeriod();
        String date = listTutorsByTimeRequest.getDate();
        List<TutorSearchDTO> tutors = subjectTutorService.findByTime(subjectId,hour, day, period).stream().filter(
                tutor -> !sessionService.verificarDispoTutor(tutor.getTutorId(), date, hour, period)
        ).collect(Collectors.toList());

        return ResponseEntity.ok(tutors);
    }
}
