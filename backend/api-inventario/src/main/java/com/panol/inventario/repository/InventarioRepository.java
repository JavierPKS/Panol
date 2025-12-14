package com.panol.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.panol.inventario.models.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {}