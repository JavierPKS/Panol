package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "DET_PRESTAMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_incio_prestamo")
    private LocalDate fechaIncioPrestamo;

    @Column(name = "fecha_retorno_prestamo")
    private LocalDate fechaRetornoPrestamo;

    @Column(name = "fecha_devolucion_prestamo")
    private LocalDate fechaDevolucionPrestamo;

    @Column(name = "PRODUCTO_id_principal")
    private Long productoIdPrincipal;

    @Column(name = "SOLI_PRESTAMO_id_prestamo")
    private Long solicitudPrestamoId;
}