package com.panol.inventario.models;

import jakarta.persistence.*;

@Entity
@Table(name = "STOCK")
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

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public Integer getStockMinimo() { return stockMinimo; }
  public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
  public Integer getStockMaximo() { return stockMaximo; }
  public void setStockMaximo(Integer stockMaximo) { this.stockMaximo = stockMaximo; }
  public Integer getCantidad() { return cantidad; }
  public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
