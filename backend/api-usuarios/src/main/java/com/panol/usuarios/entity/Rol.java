package com.panol.usuarios.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ROL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
    @Id
    @Column(name = "id_rol", length = 1)
    private String id;

    @Column(name = "nombre_rol", nullable = false, length = 25)
    private String nombre;
}