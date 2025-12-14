package com.panol.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panol.inventario.models.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {}