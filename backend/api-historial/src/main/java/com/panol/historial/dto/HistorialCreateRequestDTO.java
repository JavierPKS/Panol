package com.panol.historial.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCreateRequestDTO {

    private Integer productoId;
    private Integer cantidad;
    private LocalDate fechaInicioPrestamo;
    private LocalDate fechaRetornoPrestamo;
    private LocalDate fechaDevolucionPrestamo;
}
