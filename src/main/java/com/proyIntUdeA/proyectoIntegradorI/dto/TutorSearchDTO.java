package com.proyIntUdeA.proyectoIntegradorI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorSearchDTO {
    private String tutorId;
    private String firstName;
    private String lastName;
}
