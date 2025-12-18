package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "SOLI_PRESTAMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrestamo;

    private String estadoSolicitud;
    private Integer usuarioRut;
    private LocalDate fechaSolicitud;
    private String motivoPrestamo;
    private String prioridad;
}