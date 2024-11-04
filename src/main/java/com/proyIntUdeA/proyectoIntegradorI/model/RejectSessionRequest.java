package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RejectSessionRequest {
    private Long sessionId;
    private String tutorId;
}
