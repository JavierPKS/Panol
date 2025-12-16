package com.panol.solicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleSolicitudDTO {
    private Integer id_producto;
    private Integer cantidad;
    private LocalDate fecha_inicio;
    private LocalDate fecha_retorno;
}