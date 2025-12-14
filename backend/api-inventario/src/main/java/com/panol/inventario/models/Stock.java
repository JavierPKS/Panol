package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STOCK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_stock")
  private Integer id;

  @Column(name = "stock_minimo", nullable = false)
  private Integer stockMinimo;

  @Column(name = "stock_maximo", nullable = false)
  private Integer stockMaximo;

  @Column(name = "cantidad", nullable = false)
  private Integer cantidad;
}
