package com.panol.usuarios.service;

import com.panol.usuarios.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> listarUsuarios();
    UsuarioDTO buscarPorRut(int rut);
    UsuarioDTO crearUsuario(UsuarioDTO dto);
    UsuarioDTO editarUsuario(int rut, UsuarioDTO dto);
    void eliminarUsuario(int rut);
}