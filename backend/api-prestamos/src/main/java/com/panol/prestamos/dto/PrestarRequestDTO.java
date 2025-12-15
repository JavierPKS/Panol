package com.panol.prestamos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrestarRequestDTO {
    private Integer id_prestamo;
    private Integer id_producto;
    private Integer cantidad;
}