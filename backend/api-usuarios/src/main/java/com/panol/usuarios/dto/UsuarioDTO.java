package com.panol.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
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