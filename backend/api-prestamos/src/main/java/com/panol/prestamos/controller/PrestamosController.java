package com.panol.prestamos.controller;

import com.panol.prestamos.dto.*;
import com.panol.prestamos.service.PrestamosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<List<PrestamoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarPrestamos());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> prestar(@RequestBody PrestarRequestDTO req) {
        service.prestar(req);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Préstamo realizado con éxito");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/devolver")
    public ResponseEntity<Map<String, String>> devolver(@RequestBody DevolverRequestDTO req) {
        service.devolver(req);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Devolución registrada con éxito");
        return ResponseEntity.ok(response);
    }
}