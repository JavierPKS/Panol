package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "INVENTARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventario {
    @Id
    @Column(name = "id_inventario")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "STOCK_id_stock")
    private Stock stock;
}