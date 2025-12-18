package com.panol.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de respuesta para ubicaciones del inventario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class UbicacionResponseDTO extends RepresentationModel<UbicacionResponseDTO> {

    /**
     * Identificador de la ubicación.
     */
    private Integer id;

    /**
     * Nombre de la sala.
     */
    private String nombreSala;

    /**
     * Estante o sección.
     */
    private String estante;

    /**
     * Nivel dentro del estante.
     */
    private Integer nivel;

    /**
     * Descripción opcional.
     */
    private String descripcion;
}