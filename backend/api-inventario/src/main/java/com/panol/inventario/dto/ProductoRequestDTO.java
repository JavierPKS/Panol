package com.panol.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotNull(message = "El código interno es obligatorio")
    private Long cod_interno;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre_producto;

    @NotNull(message = "La categoría es obligatoria")
    private Integer categoria;

    @NotNull(message = "La marca es obligatoria")
    private Integer marca;

    @NotNull(message = "La cantidad inicial es obligatoria")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer cantidad;

    @NotNull(message = "La ubicación es obligatoria")
    private Integer ubicacion;
}