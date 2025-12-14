package com.panol.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.panol.inventario.models.UbicacionInv;

public interface UbicacionInvRepository extends JpaRepository<UbicacionInv, Integer> {}