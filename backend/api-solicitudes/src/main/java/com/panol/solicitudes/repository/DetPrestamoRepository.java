package com.panol.solicitudes.repository;

import com.panol.solicitudes.entity.DetPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetPrestamoRepository extends JpaRepository<DetPrestamo, Integer> {
}