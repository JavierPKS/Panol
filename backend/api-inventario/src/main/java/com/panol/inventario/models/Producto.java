package com.panol.inventario.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PRODUCTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_principal")
    private Integer id;

    @Column(name = "cod_interno", unique = true, nullable = false)
    private Long codInterno;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "estado", nullable = false)
    private String estado; // "1" Activo, "0" Eliminado

    // Relación con Inventario (que a su vez tiene Stock y Ubicación)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INVENTARIO_id_inventario", referencedColumnName = "id_inventario")
    private Inventario inventario;

    @ManyToOne
    @JoinColumn(name = "CATEGORIA_PROD_id_categoria")
    private CategoriaProd categoria;

    @ManyToOne
    @JoinColumn(name = "MARCA_id_marca")
    private Marca marca;
}