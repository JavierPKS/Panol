package com.panol.usuarios.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ROL")
public class Rol {
  @Id
  @Column(name = "id_rol")
  private String id;

  @Column(name = "nombre_rol", nullable = false)
  private String nombre;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
}