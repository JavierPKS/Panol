package com.panol.inventario.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductoEditRequest {
  private String nombreProducto;
  private Integer categoria;
  private Integer marca;
  private Integer ubicacion;
  private Integer stock;
  private String estado;
}
