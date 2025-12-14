package com.panol.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.panol.inventario.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {}
