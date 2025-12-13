package com.panol.usuarios.controller;

import com.panol.usuarios.entity.*;
import com.panol.usuarios.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

  private final UsuarioRepository usuarioRepo;
  private final RolRepository rolRepo;

  public UsuariosController(UsuarioRepository usuarioRepo, RolRepository rolRepo) {
    this.usuarioRepo = usuarioRepo;
    this.rolRepo = rolRepo;
  }

  @GetMapping
  public List<Usuario> listar() {
    return usuarioRepo.findAll();
  }

  @PostMapping
  public Map<String,String> crear(@RequestBody Map<String,Object> body) {
    Integer rut = Integer.valueOf(body.get("rut").toString());
    String dv = body.get("dv_rut").toString();
    String pnombre = body.get("pnombre").toString();
    String apPaterno = body.get("ap_paterno").toString();
    String email = body.get("email").toString();
    String rolId = body.get("rol").toString();

    Rol rol = rolRepo.findById(rolId).orElseThrow();

    Usuario u = new Usuario();
    u.setRut(rut);
    u.setDvRut(dv);
    u.setPnombre(pnombre);
    u.setApPaterno(apPaterno);
    u.setEmail(email);
    u.setActividad("1");
    u.setRol(rol);

    usuarioRepo.save(u);
    return Map.of("message","Usuario creado");
  }

  @PutMapping("/{rut}")
  public Map<String,String> editar(@PathVariable int rut, @RequestBody Map<String,Object> body) {
    Usuario u = usuarioRepo.findById(rut).orElseThrow();
    if (body.containsKey("email")) u.setEmail(body.get("email").toString());
    if (body.containsKey("actividad")) u.setActividad(body.get("actividad").toString());
    usuarioRepo.save(u);
    return Map.of("message","Usuario actualizado");
  }

  @DeleteMapping("/{rut}")
  public Map<String,String> eliminar(@PathVariable int rut) {
    usuarioRepo.deleteById(rut);
    return Map.of("message","Usuario eliminado");
  }
}
