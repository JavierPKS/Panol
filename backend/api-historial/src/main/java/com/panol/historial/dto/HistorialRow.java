package com.panol.historial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialRow {
    private String nombre_producto;
    private Integer cantidad;
    private LocalDate fecha_incio_prestamo;
    private LocalDate fecha_devolucion_prestamo;
}