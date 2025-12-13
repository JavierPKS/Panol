package com.panol.prestamos.dto;

import jakarta.validation.constraints.*;

public class PrestarRequest {
  @NotNull public Integer id_prestamo;
  @NotNull public Integer id_producto;
  @NotNull @Min(1) public Integer cantidad;
}
