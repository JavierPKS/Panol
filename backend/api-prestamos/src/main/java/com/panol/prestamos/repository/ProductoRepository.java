package com.panol.prestamos.repository;

import com.panol.prestamos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}