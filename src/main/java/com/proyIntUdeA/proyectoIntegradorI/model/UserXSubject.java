package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserXSubject {
    private Long subject_tutor_id;
    private String user_id;
    private Long subject_id;
}
