package com.panol.historial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

/**
 * DTO que representa una fila del historial de préstamos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class HistorialRow extends RepresentationModel<HistorialRow> {
    /** Identificador único del detalle de préstamo. */
    private Integer idDetalle;
    /** Nombre del producto asociado al préstamo. */
    private String nombreProducto;
    /** Cantidad de unidades prestadas. */
    private Integer cantidad;
    /** Fecha de inicio del préstamo. */
    private LocalDate fechaInicioPrestamo;
    /** Fecha programada de retorno del préstamo. */
    private LocalDate fechaRetornoPrestamo;
    /**
     * Fecha real de devolución del préstamo (puede ser nula si aún no se devuelve).
     */
    private LocalDate fechaDevolucionPrestamo;
}
