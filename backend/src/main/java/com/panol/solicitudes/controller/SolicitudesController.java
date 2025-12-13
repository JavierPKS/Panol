package com.panol.solicitudes.controller;

import com.panol.solicitudes.entity.SoliPrestamo;
import com.panol.solicitudes.repository.SoliPrestamoRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

  private final SoliPrestamoRepository repo;

  public SolicitudesController(SoliPrestamoRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<SoliPrestamo> listar() {
    return repo.findAll();
  }

  @PostMapping
  public Map<String,Object> crear(@RequestBody Map<String,Object> body) {
    Integer rut = Integer.valueOf(body.get("rut").toString());
    String motivo = body.get("motivo").toString();
    String prioridad = body.get("prioridad").toString();

    SoliPrestamo s = new SoliPrestamo();
    s.setEstado("pendiente");
    s.setRut(rut);
    s.setFecha(LocalDate.now());
    s.setMotivo(motivo);
    s.setPrioridad(prioridad);

    s = repo.save(s);
    return Map.of("message","Solicitud creada", "id", s.getId());
  }

  @PutMapping("/{id}")
  public Map<String,String> cambiarEstado(@PathVariable int id, @RequestBody Map<String,String> body) {
    SoliPrestamo s = repo.findById(id).orElseThrow();
    s.setEstado(body.get("estado"));
    repo.save(s);
    return Map.of("message","Estado actualizado");
  }

  @DeleteMapping("/{id}")
  public Map<String,String> eliminar(@PathVariable int id) {
    repo.deleteById(id);
    return Map.of("message","Solicitud eliminada");
  }
}
