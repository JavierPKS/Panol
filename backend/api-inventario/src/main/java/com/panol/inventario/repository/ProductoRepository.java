package com.panol.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panol.inventario.models.Producto;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByCodInterno(Long codInterno);

    boolean existsByCodInterno(Long codInterno);
}