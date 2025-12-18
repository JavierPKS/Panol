package com.panol.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de solicitud para crear o actualizar marcas de producto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class MarcaRequestDTO extends RepresentationModel<MarcaRequestDTO> {

    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String nombre;
}