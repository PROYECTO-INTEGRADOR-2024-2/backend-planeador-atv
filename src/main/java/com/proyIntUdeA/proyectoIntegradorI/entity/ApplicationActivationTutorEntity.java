package com.proyIntUdeA.proyectoIntegradorI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplicationActivationTutorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long applicationTutorId;
    private String userId;
    private String userSemester;
    private String applicationState;
    private Date applicationDate;
}
