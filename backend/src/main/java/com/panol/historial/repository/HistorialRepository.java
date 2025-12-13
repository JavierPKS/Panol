package com.panol.historial.repository;

import com.panol.historial.dto.HistorialRow;
import org.springframework.data.jpa.repository.*;
import java.util.List;

public interface HistorialRepository extends JpaRepository<com.panol.prestamos.entity.DetPrestamo, Integer> {

  @Query(value = """
    SELECT p.nombre_producto, dp.cantidad,
           dp.fecha_incio_prestamo, dp.fecha_devolucion_prestamo
    FROM DET_PRESTAMO dp
    JOIN PRODUCTO p ON dp.PRODUCTO_id_principal = p.id_principal
  """, nativeQuery = true)
  List<HistorialRow> general();
}
