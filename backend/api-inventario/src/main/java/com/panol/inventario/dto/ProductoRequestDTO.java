package com.panol.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de solicitud para crear productos en el inventario.
 * 
 * Contiene los datos necesarios para registrar un nuevo producto en el
 * sistema, incluyendo información básica y referencias a las entidades
 * relacionadas (categoría, marca y ubicación). Extiende
 * {@link RepresentationModel} para permitir la adición de enlaces HATEOAS en
 * las respuestas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductoRequestDTO extends RepresentationModel<ProductoRequestDTO> {

    /**
     * Código interno único del producto. Debe estar presente.
     */
    @NotNull(message = "El código interno es obligatorio")
    private Long cod_interno;

    /**
     * Nombre descriptivo del producto.
     */
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre_producto;

    /**
     * Identificador de la categoría asociada.
     */
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoria;

    /**
     * Identificador de la marca asociada.
     */
    @NotNull(message = "La marca es obligatoria")
    private Integer marca;

    /**
     * Cantidad inicial de stock. Debe ser igual o mayor a cero.
     */
    @NotNull(message = "La cantidad inicial es obligatoria")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer cantidad;

    /**
     * Identificador de la ubicación asociada.
     */
    @NotNull(message = "La ubicación es obligatoria")
    private Integer ubicacion;
}