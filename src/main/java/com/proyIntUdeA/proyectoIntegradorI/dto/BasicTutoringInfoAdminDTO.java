package com.proyIntUdeA.proyectoIntegradorI.dto;
import com.proyIntUdeA.proyectoIntegradorI.model.enums.canceledBy;
import lombok.Data;

import java.util.Date;

@Data
public class BasicTutoringInfoAdminDTO {
        private Long classId;
        private Date classDate;
        private String subjectName;
        private boolean registered;
        private canceledBy canceledBy;
        private boolean accepted;
        private String classTopics;
        private float classRate;
        private String studentId;
        private String studentFirstName;
        private String studentLastName;
        private String tutorId;
        private String tutorFirstName;
        private String tutorLastName;

    public BasicTutoringInfoAdminDTO(Long classId, Date classDate, String subjectName, boolean registered, canceledBy canceledBy, boolean accepted, String classTopics, float classRate, String studentId, String studentFirstName, String studentLastName, String tutorId, String tutorFirstName, String tutorLastName) {
        this.classId = classId;
        this.classDate = classDate;
        this.subjectName = subjectName;
        this.registered = registered;
        this.canceledBy = canceledBy;
        this.accepted = accepted;
        this.classTopics = classTopics;
        this.classRate = classRate;
        this.studentId = studentId;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.tutorId = tutorId;
        this.tutorFirstName = tutorFirstName;
        this.tutorLastName = tutorLastName;
    }
}
