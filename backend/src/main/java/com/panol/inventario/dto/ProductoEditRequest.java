package com.panol.inventario.dto;

import jakarta.validation.constraints.*;

public class ProductoEditRequest {
  @NotBlank public String nombre_producto;
  @NotNull public Integer categoria;
  @NotNull public Integer marca;
  @NotNull public Integer ubicacion;
  @NotNull @Min(0) public Integer stock;
  @NotBlank public String estado;
}
