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
    Map<String,Object> result = new HashMap<>();
    result.put("message", "Solicitud creada");
    result.put("id", s.getId());
    return result;
  }

  @PutMapping("/{id}")
  public Map<String,String> cambiarEstado(@PathVariable int id, @RequestBody Map<String,String> body) {
    SoliPrestamo s = repo.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    s.setEstado(body.get("estado"));
    repo.save(s);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Estado actualizado");
    return result;
  }

  @DeleteMapping("/{id}")
  public Map<String,String> eliminar(@PathVariable int id) {
    repo.deleteById(id);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Solicitud eliminada");
    return result;
  }
}
