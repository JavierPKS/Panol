package com.panol.prestamos.repository;

import com.panol.prestamos.entity.DetPrestamo;
import com.panol.prestamos.dto.PrestamoRow;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetPrestamoRepository extends JpaRepository<DetPrestamo, Integer> {

  @Query(value = """
    SELECT IFNULL(SUM(dp.cantidad),0)
    FROM DET_PRESTAMO dp
    WHERE dp.PRODUCTO_id_principal = :idProducto
  """, nativeQuery = true)
  int sumCantidadByProductoId(@Param("idProducto") int idProducto);

  @Query(value = """
    SELECT dp.id_detalle, p.nombre_producto, dp.cantidad,
           dp.fecha_incio_prestamo, dp.fecha_devolucion_prestamo
    FROM DET_PRESTAMO dp
    JOIN PRODUCTO p ON dp.PRODUCTO_id_principal = p.id_principal
  """, nativeQuery = true)
  List<PrestamoRow> listarPrestamos();
}
