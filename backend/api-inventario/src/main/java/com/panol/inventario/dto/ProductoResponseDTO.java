package com.panol.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de respuesta para productos.
 * 
 * Incluye información de stock y estado del producto. Extiende
 * {@link RepresentationModel} para habilitar hipermedios en la capa de
 * presentación y utiliza Lombok para la generación automática de
 * boilerplate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProductoResponseDTO extends RepresentationModel<ProductoResponseDTO> {

    private Integer id;
    private String nombre;
    private Long codigo;
    private String categoria;
    private Integer stock_disponible;
    private Integer stock_prestado;
    private Integer stock_total;
    private String estado;
}