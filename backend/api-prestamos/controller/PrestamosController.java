package com.panol.prestamos.controller;

import com.panol.prestamos.dto.*;
import com.panol.prestamos.service.PrestamosService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    Map<String,String> result = new HashMap<>();
    result.put("message", "Préstamo registrado");
    return result;
  }

  @PostMapping("/devolver")
  public Map<String,String> devolver(@Valid @RequestBody DevolverRequest req) {
    service.devolver(req);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Devolución registrada");
    return result;
  }
}
