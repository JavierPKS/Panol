package com.panol.inventario.dto;

import jakarta.validation.constraints.*;

public class ProductoRequest {
  @NotNull public Long cod_interno;
  @NotBlank public String nombre_producto;
  @NotNull public Integer categoria;
  @NotNull public Integer marca;
  @NotNull @Min(0) public Integer cantidad;
  @NotNull public Integer ubicacion;
}
