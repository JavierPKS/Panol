package com.panol.inventario.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MARCA")
public class Marca {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_marca")
  private Integer id;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
}
