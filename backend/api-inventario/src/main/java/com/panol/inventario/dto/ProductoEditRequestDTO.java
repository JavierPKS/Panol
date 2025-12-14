package com.panol.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@equalsAndHashCode(callSuper = false)

public class ProductoEditRequestDTO extends RepresentationModel<ProductoEditRequestDTO> {
  private String nombre_producto;
  private Integer categoria;
  private Integer marca;
  private Integer ubicacion;
  private Integer stock;
  private String estado;
}
