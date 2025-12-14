package com.panol.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.panol.inventario.models.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {}