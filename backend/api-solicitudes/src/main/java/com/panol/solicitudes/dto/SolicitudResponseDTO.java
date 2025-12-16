package com.panol.solicitudes.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SolicitudResponseDTO {
    private Integer id;
    private Integer rut;
    private String motivo;
    private String prioridad;
    private String estado;
    private LocalDate fechaSolicitud;
    private List<DetalleSolicitudDTO> detalles;
}