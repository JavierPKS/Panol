package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CATEGORIA_PROD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProd {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_categoria")
  private Integer id;

  @Column(name = "nombre", nullable = false)
  private String nombre;
}
