package com.proyIntUdeA.proyectoIntegradorI.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BasicTutoringInfoTutorDTO {
    private Long classId;
    private Date classDate;
    private String subjectName;
    private String classState;
    private String studentId;
    private String studentName;
    private String studentLastname;

    public BasicTutoringInfoTutorDTO(Long classId, Date classDate, String subjectName, String classState,
                                     String studentId, String studentName, String studentLastname) {
        this.classId = classId;
        this.classDate = classDate;
        this.subjectName = subjectName;
        this.classState = classState;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentLastname = studentLastname;
    }
}
