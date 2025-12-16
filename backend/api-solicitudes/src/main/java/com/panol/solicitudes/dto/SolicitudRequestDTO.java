package com.panol.solicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRequestDTO {
    private Integer rut;
    private String motivo;
    private String prioridad;
    private List<DetalleSolicitudDTO> productos;
}