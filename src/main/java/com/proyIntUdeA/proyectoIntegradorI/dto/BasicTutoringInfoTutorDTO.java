package com.proyIntUdeA.proyectoIntegradorI.dto;

import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import lombok.Data;

import java.util.Date;

@Data
public class BasicTutoringInfoTutorDTO {
    private Long classId;
    private String classDate;
    private String subjectName;
    private boolean registered;
    private canceledBy canceledBy;
    private boolean accepted;
    private String classTopics;
    private float classRate;
    private String studentId;
    private String studentName;
    private String studentLastname;

    public BasicTutoringInfoTutorDTO(Long classId, String classDate, String subjectName, boolean registered, canceledBy canceledBy, boolean accepted, String classTopics, float classRate, String studentId, String studentName, String studentLastname) {
        this.classId = classId;
        this.classDate = classDate;
        this.subjectName = subjectName;
        this.registered = registered;
        this.canceledBy = canceledBy;
        this.accepted = accepted;
        this.classTopics = classTopics;
        this.classRate = classRate;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentLastname = studentLastname;
    }
}
