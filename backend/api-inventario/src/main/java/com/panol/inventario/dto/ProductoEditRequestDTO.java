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
 * DTO de solicitud para editar un producto existente.
 * 
 * Permite actualizar información básica del producto y su inventario. Se
 * extiende de {@link RepresentationModel} para facilitar la integración con
 * HATEOAS y se utilizan anotaciones de Lombok para reducir la verbosidad.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductoEditRequestDTO extends RepresentationModel<ProductoEditRequestDTO> {

    /**
     * Nombre actualizado del producto. Obligatorio.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre_producto;

    /**
     * Identificador de la categoría asociada. Obligatorio.
     */
    @NotNull(message = "Categoría obligatoria")
    private Integer categoria;

    /**
     * Identificador de la marca asociada. Obligatorio.
     */
    @NotNull(message = "Marca obligatoria")
    private Integer marca;

    /**
     * Identificador de la ubicación asociada. Obligatorio.
     */
    @NotNull(message = "Ubicación obligatoria")
    private Integer ubicacion;

    /**
     * Cantidad total de stock. Debe ser cero o un valor positivo.
     */
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    /**
     * Estado del producto (por ejemplo, activo o eliminado). Opcional.
     */
    private String estado;
}