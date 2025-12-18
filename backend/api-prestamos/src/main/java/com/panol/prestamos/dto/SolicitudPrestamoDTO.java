package com.panol.prestamos.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class SolicitudPrestamoDTO extends RepresentationModel<SolicitudPrestamoDTO> {

    private Long idPrestamo;
    private String estadoSolicitud;
    private Integer usuarioRut;
    private LocalDate fechaSolicitud;
    private String motivoPrestamo;
    private String prioridad;
}
