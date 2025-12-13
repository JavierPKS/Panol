package com.panol.usuarios.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
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

  public Integer getRut() { return rut; }
  public void setRut(Integer rut) { this.rut = rut; }
  public String getDvRut() { return dvRut; }
  public void setDvRut(String dvRut) { this.dvRut = dvRut; }
  public String getPnombre() { return pnombre; }
  public void setPnombre(String pnombre) { this.pnombre = pnombre; }
  public String getSnombre() { return snombre; }
  public void setSnombre(String snombre) { this.snombre = snombre; }
  public String getApPaterno() { return apPaterno; }
  public void setApPaterno(String apPaterno) { this.apPaterno = apPaterno; }
  public String getApMaterno() { return apMaterno; }
  public void setApMaterno(String apMaterno) { this.apMaterno = apMaterno; }
  public String getActividad() { return actividad; }
  public void setActividad(String actividad) { this.actividad = actividad; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public Rol getRol() { return rol; }
  public void setRol(Rol rol) { this.rol = rol; }
}
