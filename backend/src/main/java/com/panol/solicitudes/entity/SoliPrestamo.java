package com.panol.solicitudes.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SOLI_PRESTAMO")
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

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public Integer getRut() { return rut; }
  public void setRut(Integer rut) { this.rut = rut; }
  public LocalDate getFecha() { return fecha; }
  public void setFecha(LocalDate fecha) { this.fecha = fecha; }
  public String getMotivo() { return motivo; }
  public void setMotivo(String motivo) { this.motivo = motivo; }
  public String getPrioridad() { return prioridad; }
  public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
}
