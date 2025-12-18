package com.panol.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de solicitud para crear o actualizar categorías de producto.
 * 
 * Este DTO extiende {@link RepresentationModel} para habilitar la capacidad
 * de agregar enlaces HATEOAS en las respuestas. Todas las anotaciones de
 * Lombok están presentes para generar getters, setters, constructores y
 * métodos equals/hashCode sin necesidad de escribir código repetitivo. La
 * validación de campos se realiza mediante anotaciones de Jakarta Validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CategoriaRequestDTO extends RepresentationModel<CategoriaRequestDTO> {

    /**
     * Nombre descriptivo de la categoría. Es obligatorio y no puede ser vacío.
     */
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;
}