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
    private long application_tutor_id;
    private String user_id;
    private String application_state;
    private Date application_date;

}
