package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "INVENTARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_inventario")
  private Integer id;

  @Column(name = "observacion")
  private String observacion;

  @Column(name = "fecha_actualizacion")
  private LocalDate fechaActualizacion;

  @ManyToOne
  @JoinColumn(name = "UBICACION_INV_id_ubicacion")
  private UbicacionInv ubicacion;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "STOCK_id_stock")
  private Stock stock;
}