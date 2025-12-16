package com.panol.solicitudes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudRequestDTO {

    @NotNull(message = "El RUT es obligatorio")
    private Integer rut_usuario;

    @NotNull(message = "El motivo es obligatorio")
    private String motivo_prestamo;

    @NotNull(message = "La prioridad es obligatoria")
    private String prioridad;

    @Valid
    @Size(min = 1, message = "Debe solicitar al menos un producto")
    private List<DetalleSolicitudDTO> productos;
}