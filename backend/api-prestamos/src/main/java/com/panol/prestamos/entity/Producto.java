package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    @Id
    @Column(name = "id_principal")
    private Integer id; // No generamos ID aquí, solo leemos

    @Column(name = "nombre_producto")
    private String nombre;

    @OneToOne
    @JoinColumn(name = "INVENTARIO_id_inventario")
    private Inventario inventario;
}