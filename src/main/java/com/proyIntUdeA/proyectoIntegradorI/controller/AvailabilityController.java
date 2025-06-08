package com.proyIntUdeA.proyectoIntegradorI.controller;

import com.proyIntUdeA.proyectoIntegradorI.dto.TutorSearchDTO;
import com.proyIntUdeA.proyectoIntegradorI.entity.SlotAvailabilityEntity;
import com.proyIntUdeA.proyectoIntegradorI.model.ListTutorsByTimeRequest;
import com.proyIntUdeA.proyectoIntegradorI.service.SubjectTutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class AvailabilityController {
    private final SubjectTutorService subjectTutorService;

    public AvailabilityController(SubjectTutorService subjectTutorService) {
        this.subjectTutorService = subjectTutorService;
    }

    @GetMapping("/listarTutores")
    public ResponseEntity<List<TutorSearchDTO>> findTutorsByTime(@RequestBody ListTutorsByTimeRequest listTutorsByTimeRequest){
        Long subjectId = listTutorsByTimeRequest.getSubjectId();
        String hour = listTutorsByTimeRequest.getHour();
        String day = listTutorsByTimeRequest.getDayWeek();
        List<TutorSearchDTO> tutors = subjectTutorService.findByTime(subjectId,hour, day );
        return ResponseEntity.ok(tutors);
    }
}
