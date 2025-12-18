package com.panol.solicitudes.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
    import java.time.LocalDate;
    import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class SolicitudResponseDTO extends RepresentationModel<SolicitudResponseDTO> {
    private Integer id;
    private Integer rut;
    private String motivo;
    private String prioridad;
    private String estado;
    private LocalDate fechaSolicitud;
    private List<DetalleSolicitudDTO> detalles;
}