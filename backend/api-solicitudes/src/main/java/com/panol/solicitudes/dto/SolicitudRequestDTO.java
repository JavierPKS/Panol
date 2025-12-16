package com.panol.solicitudes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class SolicitudRequestDTO {

    @NotNull(message = "El RUT es obligatorio")
    private Integer rut;

    @NotNull(message = "El motivo es obligatorio")
    private String motivo;

    @NotNull(message = "La prioridad es obligatoria")
    private String prioridad;

    @Size(min = 1, message = "Debe solicitar al menos un producto")
    private List<DetalleSolicitudDTO> productos;
}