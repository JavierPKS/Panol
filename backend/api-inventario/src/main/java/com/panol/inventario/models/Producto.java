package com.panol.inventario.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTO")
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

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public Long getCodInterno() { return codInterno; }
  public void setCodInterno(Long codInterno) { this.codInterno = codInterno; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public String getNombreProducto() { return nombreProducto; }
  public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
  public Inventario getInventario() { return inventario; }
  public void setInventario(Inventario inventario) { this.inventario = inventario; }
  public CategoriaProd getCategoria() { return categoria; }
  public void setCategoria(CategoriaProd categoria) { this.categoria = categoria; }
  public Marca getMarca() { return marca; }
  public void setMarca(Marca marca) { this.marca = marca; }
}
