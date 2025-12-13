package com.panol.inventario.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "INVENTARIO")
public class Inventario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_inventario")
  private Integer id;

  @Column(name = "observacion")
  private String observacion;

  @Column(name = "fecha_actualizacion", nullable = false)
  private LocalDate fechaActualizacion;

  @ManyToOne(optional = false)
  @JoinColumn(name = "UBICACION_INV_id_ubicacion", nullable = false)
  private UbicacionInv ubicacion;

  @ManyToOne(optional = false)
  @JoinColumn(name = "STOCK_id_stock", nullable = false)
  private Stock stock;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getObservacion() { return observacion; }
  public void setObservacion(String observacion) { this.observacion = observacion; }
  public LocalDate getFechaActualizacion() { return fechaActualizacion; }
  public void setFechaActualizacion(LocalDate fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
  public UbicacionInv getUbicacion() { return ubicacion; }
  public void setUbicacion(UbicacionInv ubicacion) { this.ubicacion = ubicacion; }
  public Stock getStock() { return stock; }
  public void setStock(Stock stock) { this.stock = stock; }
}
