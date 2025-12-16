package com.panol.historial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "DET_PRESTAMO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCTO_id_principal", nullable = false)
  private Producto producto;
}