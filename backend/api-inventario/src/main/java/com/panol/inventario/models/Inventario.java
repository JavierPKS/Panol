package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "INVENTARIO")
@Getter
@Setter
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

  @Column(name = "fecha_actualizacion", nullable = false)
  private LocalDate fechaActualizacion;

  @ManyToOne(optional = false)
  @JoinColumn(name = "UBICACION_INV_id_ubicacion", nullable = false)
  private UbicacionInv ubicacion;

  @ManyToOne(optional = false)
  @JoinColumn(name = "STOCK_id_stock", nullable = false)
  private Stock stock;
}
