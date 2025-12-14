package com.panol.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@equalsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductoRequestDTO extends RepresentationModel<ProductoRequestDTO> {
  private Long cod_interno;
  private String nombre_producto;
  private Integer categoria;
  private Integer marca;
  private Integer cantidad;
  private Integer ubicacion;
}
