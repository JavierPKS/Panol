package com.panol.prestamos.dto;

import java.time.LocalDate;

public interface PrestamoRow {
  Integer getId_detalle();
  String getNombre_producto();
  Integer getCantidad();
  LocalDate getFecha_incio_prestamo();
  LocalDate getFecha_devolucion_prestamo();
}
