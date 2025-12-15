package com.panol.prestamos.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PrestamoResponseDTO {
    private Integer id_detalle;
    private String nombre_producto;
    private Integer cantidad;
    private LocalDate fecha_incio_prestamo;
    private LocalDate fecha_devolucion_prestamo;
}