package com.panol.prestamos.repository;

import com.panol.prestamos.entity.SolicitudPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudPrestamoRepository extends JpaRepository<SolicitudPrestamo, Long> {
}
