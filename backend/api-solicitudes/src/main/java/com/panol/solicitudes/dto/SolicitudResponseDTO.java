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
    private Integer id_solicitud;
    private Integer rut_usuario;
    private String motivo_prestamo;
    private String prioridad;
    private String estado_solicitud;
    private LocalDate fecha_solicitud;
    private List<DetalleSolicitudDTO> detalle_productos;
}