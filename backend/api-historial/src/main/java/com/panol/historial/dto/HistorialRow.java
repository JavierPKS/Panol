package com.panol.historial.dto;

import java.time.LocalDate;

@Data
public interface HistorialRow {
  String getNombre_producto();

  Integer getCantidad();

  LocalDate getFecha_incio_prestamo();

  LocalDate getFecha_devolucion_prestamo();
}
