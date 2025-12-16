package com.panol.usuarios.service.impl;

import com.panol.usuarios.dto.UsuarioDTO;
import com.panol.usuarios.entity.Rol;
import com.panol.usuarios.entity.Usuario;
import com.panol.usuarios.repository.RolRepository;
import com.panol.usuarios.repository.UsuarioRepository;
import com.panol.usuarios.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepo, RolRepository rolRepo) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepo.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorRut(int rut) {
        Usuario u = usuarioRepo.findById(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con RUT: " + rut));
        return mapearADTO(u);
    }

    @Override
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        if (usuarioRepo.existsById(dto.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }
        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El Email ya está registrado");
        }

        Rol rol = rolRepo.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("El Rol especificado no existe"));

        Usuario u = Usuario.builder()
                .rut(dto.getRut())
                .dvRut(dto.getDvRut())
                .pnombre(dto.getPnombre())
                .snombre(dto.getSnombre())
                .apPaterno(dto.getApPaterno())
                .apMaterno(dto.getApMaterno())
                .email(dto.getEmail())
                .actividad("1") // Por defecto activo al crear
                .rol(rol)
                .build();

        return mapearADTO(usuarioRepo.save(u));
    }

    @Override
    @Transactional
    public UsuarioDTO editarUsuario(int rut, UsuarioDTO dto) {
        Usuario u = usuarioRepo.findById(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizamos campos permitidos
        if(dto.getPnombre() != null) u.setPnombre(dto.getPnombre());
        if(dto.getSnombre() != null) u.setSnombre(dto.getSnombre());
        if(dto.getApPaterno() != null) u.setApPaterno(dto.getApPaterno());
        if(dto.getApMaterno() != null) u.setApMaterno(dto.getApMaterno());
        if(dto.getEmail() != null) u.setEmail(dto.getEmail());
        if(dto.getActividad() != null) u.setActividad(dto.getActividad());

        if (dto.getIdRol() != null) {
            Rol nuevoRol = rolRepo.findById(dto.getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            u.setRol(nuevoRol);
        }

        return mapearADTO(usuarioRepo.save(u));
    }

    @Override
    @Transactional
    public void eliminarUsuario(int rut) {
        if (!usuarioRepo.existsById(rut)) {
            throw new RuntimeException("Usuario no encontrado para eliminar");
        }
        usuarioRepo.deleteById(rut);
    }

    // Método auxiliar para convertir Entidad -> DTO
    private UsuarioDTO mapearADTO(Usuario u) {
        return UsuarioDTO.builder()
                .rut(u.getRut())
                .dvRut(u.getDvRut())
                .pnombre(u.getPnombre())
                .snombre(u.getSnombre())
                .apPaterno(u.getApPaterno())
                .apMaterno(u.getApMaterno())
                .email(u.getEmail())
                .actividad(u.getActividad())
                .idRol(u.getRol().getId())
                .nombreRol(u.getRol().getNombre())
                .build();
    }
}