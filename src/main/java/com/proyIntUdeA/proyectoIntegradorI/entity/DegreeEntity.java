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
    public long degree_id;
    public String degree_name;
    public String degree_modality;
    public String degree_department;
}

