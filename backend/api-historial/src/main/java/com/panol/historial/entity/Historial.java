package com.panol.historial.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DET_PRESTAMO")
public class Historial {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_detalle")
  private Integer idDetalle;

  @Column(name = "cantidad", nullable = false)
  private Integer cantidad;

  @Column(name = "fecha_incio_prestamo", nullable = false)
  private LocalDate fechaInicioPrestamo;

  @Column(name = "fecha_retorno_prestamo", nullable = false)
  private LocalDate fechaRetornoPrestamo;

  @Column(name = "fecha_devolucion_prestamo")
  private LocalDate fechaDevolucionPrestamo;

  @Column(name = "SOLI_PRESTAMO_id_prestamo", nullable = false)
  private Integer idPrestamo;

  @Column(name = "PRODUCTO_id_principal", nullable = false)
  private Integer idProducto;

  public Integer getIdDetalle() { return idDetalle; }
  public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }
  public Integer getCantidad() { return cantidad; }
  public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
  public LocalDate getFechaInicioPrestamo() { return fechaInicioPrestamo; }
  public void setFechaInicioPrestamo(LocalDate fechaInicioPrestamo) { this.fechaInicioPrestamo = fechaInicioPrestamo; }
  public LocalDate getFechaRetornoPrestamo() { return fechaRetornoPrestamo; }
  public void setFechaRetornoPrestamo(LocalDate fechaRetornoPrestamo) { this.fechaRetornoPrestamo = fechaRetornoPrestamo; }
  public LocalDate getFechaDevolucionPrestamo() { return fechaDevolucionPrestamo; }
  public void setFechaDevolucionPrestamo(LocalDate fechaDevolucionPrestamo) { this.fechaDevolucionPrestamo = fechaDevolucionPrestamo; }
  public Integer getIdPrestamo() { return idPrestamo; }
  public void setIdPrestamo(Integer idPrestamo) { this.idPrestamo = idPrestamo; }
  public Integer getIdProducto() { return idProducto; }
  public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
}
