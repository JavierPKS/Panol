package com.panol.prestamos.controller;

import com.panol.prestamos.dto.*;
import com.panol.prestamos.service.PrestamosService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamosController {

  private final PrestamosService service;

  public PrestamosController(PrestamosService service) {
    this.service = service;
  }

  @GetMapping
  public List<PrestamoRow> listar() {
    return service.listar();
  }

  @PostMapping
  public Map<String,String> prestar(@Valid @RequestBody PrestarRequest req) {
    service.prestar(req);
    return Map.of("message", "Préstamo registrado");
  }

  @PostMapping("/devolver")
  public Map<String,String> devolver(@Valid @RequestBody DevolverRequest req) {
    service.devolver(req);
    return Map.of("message", "Devolución registrada");
  }
}
