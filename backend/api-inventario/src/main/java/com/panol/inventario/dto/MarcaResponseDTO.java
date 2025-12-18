package com.panol.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de respuesta para marcas de producto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class MarcaResponseDTO extends RepresentationModel<MarcaResponseDTO> {

    /**
     * Identificador de la marca.
     */
    private Integer id;

    /**
     * Nombre de la marca.
     */
    private String nombre;
}