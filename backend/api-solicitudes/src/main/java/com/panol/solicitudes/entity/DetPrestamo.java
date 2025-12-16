package com.panol.solicitudes.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "DET_PRESTAMO")
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

    @Column(name = "PRODUCTO_id_principal", nullable = false)
    private Integer idProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOLI_PRESTAMO_id_prestamo", nullable = false)
    private SoliPrestamo solicitud;
}