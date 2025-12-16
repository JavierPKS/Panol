package com.panol.solicitudes.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DetalleSolicitudDTO {
    private Integer idProducto;
    private Integer cantidad;
    private LocalDate fechaInicio;
    private LocalDate fechaRetorno;
}