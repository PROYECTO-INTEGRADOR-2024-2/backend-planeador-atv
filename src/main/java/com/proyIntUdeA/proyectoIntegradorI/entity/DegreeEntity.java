package com.proyIntUdeA.proyectoIntegradorI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "degree")
public class DegreeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long degreeId;
    public String degreeName;
    public String degreeModality;
    public String degreeDepartment;
}

