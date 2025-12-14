package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UBICACION_INV")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
