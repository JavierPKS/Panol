package com.panol.prestamos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestarRequestDTO {
    private Integer id_prestamo;
    private Integer id_producto;
    private Integer cantidad;
}