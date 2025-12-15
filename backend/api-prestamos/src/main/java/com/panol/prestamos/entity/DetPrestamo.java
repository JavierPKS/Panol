package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "DET_PRESTAMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetPrestamo {

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
  private Integer idPrestamo; // Mantenemos el ID de solicitud por simplicidad

  @ManyToOne
  @JoinColumn(name = "PRODUCTO_id_principal", nullable = false)
  private Producto producto;
}