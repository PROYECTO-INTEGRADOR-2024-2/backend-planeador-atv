package com.proyIntUdeA.proyectoIntegradorI.dto;

import lombok.Data;

import java.util.Date;
@Data
public class BasicTutoringInfoDTO {
    private Date classDate;
    private String subjectName;
    private String classState;
    private String tutorId;
    private String tutorName;
    private String tutorLastname;

    public BasicTutoringInfoDTO(Date classDate, String subjectName, String classState,
                                String tutorId, String tutorName, String tutorLastname) {
        this.classDate = classDate;
        this.subjectName = subjectName;
        this.classState = classState;
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.tutorLastname = tutorLastname;
    }
}