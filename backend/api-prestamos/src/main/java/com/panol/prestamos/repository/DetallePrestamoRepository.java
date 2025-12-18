package com.panol.prestamos.repository;

import com.panol.prestamos.entity.DetallePrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Long> {

    List<DetallePrestamo> findBySolicitudPrestamoId(Long solicitudPrestamoId);
}
