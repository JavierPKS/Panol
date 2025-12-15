package com.panol.prestamos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "STOCK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Integer id;

    @Column(name = "cantidad")
    private Integer cantidad;
}