package com.panol.usuarios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Integer rut;
    private String dvRut;
    private String pnombre;
    private String snombre;
    private String apPaterno;
    private String apMaterno;
    private String email;
    private String idRol;     
    private String nombreRol; 
    private String actividad;
}