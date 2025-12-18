package com.panol.prestamos.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class DetallePrestamoDTO extends RepresentationModel<DetallePrestamoDTO> {

    private Long idDetalle;
    private Integer cantidad;
    private LocalDate fechaIncioPrestamo;
    private LocalDate fechaRetornoPrestamo;
    private LocalDate fechaDevolucionPrestamo;
    private Long productoIdPrincipal;
    private Long solicitudPrestamoId;
}
