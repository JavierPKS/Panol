package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToOne
    @JoinColumn(name = "STOCK_id_stock")
    private Stock stock;
}