package com.panol.solicitudes.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "SOLI_PRESTAMO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoliPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer id;

    @Column(name = "estado_solicitud", nullable = false)
    private String estado;

    @Column(name = "USUARIO_rut", nullable = false)
    private Integer rut;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fecha;

    @Column(name = "motivo_prestamo", nullable = false)
    private String motivo;

    @Column(name = "prioridad", nullable = false)
    private String prioridad;
    
    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetPrestamo> detalles;
}