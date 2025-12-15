package com.panol.prestamos.repository;

import com.panol.prestamos.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}