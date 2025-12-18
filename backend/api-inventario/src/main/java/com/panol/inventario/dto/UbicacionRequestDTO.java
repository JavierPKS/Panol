package com.panol.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de solicitud para crear o actualizar ubicaciones de inventario.
 * 
 * Incluye validaciones básicas para garantizar que los campos obligatorios
 * estén presentes al momento de recibir la solicitud.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class UbicacionRequestDTO extends RepresentationModel<UbicacionRequestDTO> {

    /**
     * Nombre de la sala o recinto donde se encuentra el producto.
     */
    @NotBlank(message = "El nombre de la sala es obligatorio")
    private String nombreSala;

    /**
     * Identificador del estante o sección.
     */
    @NotBlank(message = "El estante es obligatorio")
    private String estante;

    /**
     * Nivel o posición dentro del estante. Puede ser cero o un número positivo.
     */
    @NotNull(message = "El nivel es obligatorio")
    private Integer nivel;

    /**
     * Descripción adicional opcional de la ubicación.
     */
    private String descripcion;
}