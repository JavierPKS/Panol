package com.panol.solicitudes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private Integer rut;
    
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
    
    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad;
    
    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid 
    private List<DetalleSolicitudDTO> productos;
}