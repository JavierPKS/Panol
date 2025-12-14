package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_principal")
  private Integer id;

  @Column(name = "cod_interno", nullable = false, unique = true)
  private Long codInterno;

  @Column(name = "estado", nullable = false)
  private String estado;

  @Column(name = "nombre_producto", nullable = false)
  private String nombreProducto;

  @ManyToOne(optional = false)
  @JoinColumn(name = "INVENTARIO_id_inventario", nullable = false)
  private Inventario inventario;

  @ManyToOne(optional = false)
  @JoinColumn(name = "CATEGORIA_PROD_id_categoria", nullable = false)
  private CategoriaProd categoria;

  @ManyToOne(optional = false)
  @JoinColumn(name = "MARCA_id_marca", nullable = false)
  private Marca marca;
}
