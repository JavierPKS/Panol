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
    private Integer idProducto;
    private Integer cantidad;
    private LocalDate fechaInicio;
    private LocalDate fechaRetorno;
}