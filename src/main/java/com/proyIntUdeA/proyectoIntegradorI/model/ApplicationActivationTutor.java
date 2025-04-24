package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationActivationTutor {
    private long applicationTutorId;
    private String userId;
    private String applicationId;
    private Date applicationDate;
}
