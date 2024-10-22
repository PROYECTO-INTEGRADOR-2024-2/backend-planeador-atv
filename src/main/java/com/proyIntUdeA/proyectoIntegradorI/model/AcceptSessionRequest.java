package com.proyIntUdeA.proyectoIntegradorI.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AcceptSessionRequest {
    private long sessionId;
    private String tutorId;
}
