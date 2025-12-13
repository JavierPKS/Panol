package com.panol.inventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UBICACION_INV")
public class UbicacionInv {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_ubicacion")
  private Integer id;

  @Column(name = "nombre_sala", nullable = false)
  private String nombreSala;

  @Column(name = "estante", nullable = false)
  private String estante;

  @Column(name = "nivel", nullable = false)
  private Integer nivel;

  @Column(name = "descripcion")
  private String descripcion;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getNombreSala() { return nombreSala; }
  public void setNombreSala(String nombreSala) { this.nombreSala = nombreSala; }
  public String getEstante() { return estante; }
  public void setEstante(String estante) { this.estante = estante; }
  public Integer getNivel() { return nivel; }
  public void setNivel(Integer nivel) { this.nivel = nivel; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
