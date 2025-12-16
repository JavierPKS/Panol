package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "STOCK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_stock")
  private Integer id;

  @Column(name = "stock_minimo")
  private Integer stockMinimo;

  @Column(name = "stock_maximo")
  private Integer stockMaximo;

  @Column(name = "cantidad")
  private Integer cantidad;
}