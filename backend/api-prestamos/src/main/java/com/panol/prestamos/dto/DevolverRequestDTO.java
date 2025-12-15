package com.panol.prestamos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DevolverRequestDTO {
    private Integer id_detalle;
    private Integer cantidad;
}