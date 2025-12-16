package com.panol.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEditRequestDTO {

  @NotBlank(message = "El nombre es obligatorio")
  private String nombre_producto;

  @NotNull(message = "Categoría obligatoria")
  private Integer categoria;

  @NotNull(message = "Marca obligatoria")
  private Integer marca;

  @NotNull(message = "Ubicación obligatoria")
  private Integer ubicacion;

  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  private String estado;
}