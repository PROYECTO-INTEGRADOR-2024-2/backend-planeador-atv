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
    private long application_tutor_id;
    private String user_id;
    private String application_state;
    private Date application_date;
}
