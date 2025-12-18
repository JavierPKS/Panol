package com.panol.prestamos.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CrearSolicitudPrestamoDTO extends RepresentationModel<CrearSolicitudPrestamoDTO> {

    private Integer usuarioRut;
    private String motivoPrestamo;
    private String prioridad;
    private List<CrearDetallePrestamoDTO> detalles;
}
