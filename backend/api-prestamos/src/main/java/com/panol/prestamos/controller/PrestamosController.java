package com.panol.prestamos.controller;

import com.panol.prestamos.dto.*;
import com.panol.prestamos.service.PrestamosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<PrestamoResponseDTO>> listar() {
    return ResponseEntity.ok(service.listarPrestamos());
  }

  @PostMapping
  public ResponseEntity<Map<String,String>> prestar(@Valid @RequestBody PrestarRequestDTO req) {
    service.prestar(req);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Préstamo registrado correctamente");
    return ResponseEntity.ok(result);
  }

  @PostMapping("/devolver")
  public ResponseEntity<Map<String,String>> devolver(@Valid @RequestBody DevolverRequestDTO req) {
    service.devolver(req);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Devolución registrada correctamente");
    return ResponseEntity.ok(result);
  }
}