package com.proyIntUdeA.proyectoIntegradorI.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorActivationRequest {
    private Long requestId;
    private String requestDate;
    private String requestState;
    private String studentId;
    private String studentName;
    private String semester;
    private String fileUrl;
}
