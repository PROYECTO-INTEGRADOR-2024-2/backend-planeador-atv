package com.proyIntUdeA.proyectoIntegradorI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicTutoInfoDTO {
    private String fecha;
}
