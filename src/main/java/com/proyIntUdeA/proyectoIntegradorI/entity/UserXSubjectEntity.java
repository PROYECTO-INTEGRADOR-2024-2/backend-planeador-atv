package com.proyIntUdeA.proyectoIntegradorI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserXSubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subject_tutor_id;
    private String user_id;
    private String subject_id;
}
