package com.proyIntUdeA.proyectoIntegradorI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorAvailabilityRequestDTO {
    private String tutorId;
    private List<AvailabilitySlotDTO> disponibilidad;
    private int totalBloques;
}
