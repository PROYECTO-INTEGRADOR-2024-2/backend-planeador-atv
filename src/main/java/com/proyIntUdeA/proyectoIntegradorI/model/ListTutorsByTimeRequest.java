package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ListTutorsByTimeRequest {
    private Long subjectId;
    private String date;
    private String hour;
    private String dayWeek;
    private String period;
}
