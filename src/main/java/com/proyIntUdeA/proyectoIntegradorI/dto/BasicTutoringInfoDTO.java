package com.proyIntUdeA.proyectoIntegradorI.dto;

import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import lombok.Data;

import java.util.Date;
@Data
public class BasicTutoringInfoDTO {
    private Long classId;
    private Date classDate;
    private String subjectName;
    private boolean registered;
    private canceledBy canceledBy;
    private boolean accepted;
    private String classTopics;
    private float classRate;
    private String tutorId;
    private String tutorName;
    private String tutorLastname;

    public BasicTutoringInfoDTO(Long classId, Date classDate, String subjectName, boolean registered, canceledBy canceledBy, boolean accepted, String classTopics, float classRate, String tutorId, String tutorName, String tutorLastname) {
        this.classId = classId;
        this.classDate = classDate;
        this.subjectName = subjectName;
        this.registered = registered;
        this.canceledBy = canceledBy;
        this.accepted = accepted;
        this.classTopics = classTopics;
        this.classRate = classRate;
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.tutorLastname = tutorLastname;
    }
}