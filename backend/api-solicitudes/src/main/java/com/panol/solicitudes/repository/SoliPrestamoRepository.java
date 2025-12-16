package com.panol.solicitudes.repository;

import com.panol.solicitudes.entity.SoliPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoliPrestamoRepository extends JpaRepository<SoliPrestamo, Integer> {
}