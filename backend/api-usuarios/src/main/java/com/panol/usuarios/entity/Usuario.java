package com.panol.usuarios.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

  @Id
  @Column(name = "rut")
  private Integer rut;

  @Column(name = "dv_rut", nullable = false)
  private String dvRut;

  @Column(name = "pnombre", nullable = false)
  private String pnombre;

  @Column(name = "snombre")
  private String snombre;

  @Column(name = "ap_paterno", nullable = false)
  private String apPaterno;

  @Column(name = "ap_materno")
  private String apMaterno;

  @Column(name = "actividad", nullable = false)
  private String actividad;

  @Column(name = "email", nullable = false)
  private String email;

  @ManyToOne(optional = false)
  @JoinColumn(name = "ROL_id_rol", nullable = false)
  private Rol rol;
}
