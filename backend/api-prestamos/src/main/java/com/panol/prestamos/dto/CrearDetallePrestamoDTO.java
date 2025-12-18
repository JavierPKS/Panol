package com.panol.prestamos.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CrearDetallePrestamoDTO extends RepresentationModel<CrearDetallePrestamoDTO> {

    private Long productoIdPrincipal;
    private Integer cantidad;
}
