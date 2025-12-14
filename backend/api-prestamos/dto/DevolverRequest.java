package com.panol.prestamos.dto;

import jakarta.validation.constraints.*;

public class DevolverRequest {
  @NotNull public Integer id_detalle;
  @NotNull @Min(1) public Integer cantidad;
  @NotNull public Integer id_producto;
}
