package com.proyIntUdeA.proyectoIntegradorI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilitySlotDTO {
    private String dia;
    private String horaInicio;
    private String horaFinal;
    private String periodo;
}
