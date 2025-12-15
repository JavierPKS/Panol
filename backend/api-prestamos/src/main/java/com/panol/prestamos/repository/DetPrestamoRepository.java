package com.panol.prestamos.repository;

import com.panol.prestamos.entity.DetPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetPrestamoRepository extends JpaRepository<DetPrestamo, Integer> {
    List<DetPrestamo> findByProductoId(Integer idProducto);
}