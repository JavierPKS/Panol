package com.panol.inventario.dto;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.validation.constraints.*;

@Data
@equalsAndHashCode(callSuper = false)
public class ProductoEditRequest {
  private String nombre_producto;
  private Integer categoria;
  private Integer marca;
  private Integer ubicacion;
  private Integer stock;
  private String estado;
}
