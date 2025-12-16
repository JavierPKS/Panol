package com.panol.solicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudResponseDTO {
    private Integer id;
    private Integer rut;
    private String motivo;
    private String prioridad;
    private String estado;
    private LocalDate fechaSolicitud;
    private List<DetalleSolicitudDTO> detalles;
}