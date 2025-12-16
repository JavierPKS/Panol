package com.panol.usuarios.controller;

import com.panol.usuarios.dto.UsuarioDTO;
import com.panol.usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
// Configuración CORS básica para desarrollo
@CrossOrigin(origins = "*") 
public class UsuariosController {

    private final UsuarioService usuarioService;

    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{rut}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable int rut) {
        return ResponseEntity.ok(usuarioService.buscarPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{rut}")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable int rut, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.editarUsuario(rut, dto));
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable int rut) {
        usuarioService.eliminarUsuario(rut);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }
}