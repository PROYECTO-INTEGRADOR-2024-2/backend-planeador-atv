package com.proyIntUdeA.proyectoIntegradorI.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "class")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long classId;
    private String classState;
    private String studentId;
    private String tutorId;
    private long subjectId;
    private String classTopics;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date classDate;
    private float classRate;
}
