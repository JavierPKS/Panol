package com.panol.solicitudes.repository;

import com.panol.solicitudes.entity.SoliPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoliPrestamoRepository extends JpaRepository<SoliPrestamo, Integer> {
}