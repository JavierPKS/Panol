package com.panol.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de respuesta para categorías de producto.
 * 
 * Extiende {@link RepresentationModel} para posibilitar la inclusión de
 * hiperenlaces HATEOAS. Contiene el identificador y el nombre de la
 * categoría almacenada en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CategoriaResponseDTO extends RepresentationModel<CategoriaResponseDTO> {

    /**
     * Identificador único de la categoría.
     */
    private Integer id;

    /**
     * Nombre descriptivo de la categoría.
     */
    private String nombre;
}