package com.panol.usuarios.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ROL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
  @Id
  @Column(name = "id_rol")
  private String id;

  @Column(name = "nombre_rol", nullable = false)
  private String nombre;
}