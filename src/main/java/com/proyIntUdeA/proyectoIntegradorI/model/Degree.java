package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Degree {

    private long degreeId;
    private String degreeName;
    private String degreeModality;
    private String degreeDepartment;


    public Degree(Long degree_id, String degree_name, String degree_modality, String degree_department) {
        this.degreeId = degree_id;
        this.degreeName = degree_name;
        this.degreeModality = degree_modality;
        this.degreeDepartment = degree_department;
    }
}
