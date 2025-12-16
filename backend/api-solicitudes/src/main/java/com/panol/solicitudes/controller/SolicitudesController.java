package com.panol.solicitudes.controller;

import com.panol.solicitudes.dto.SolicitudRequestDTO;
import com.panol.solicitudes.dto.SolicitudResponseDTO;
import com.panol.solicitudes.service.SolicitudesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

    private final SolicitudesService service;

    public SolicitudesController(SolicitudesService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarSolicitudes());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody SolicitudRequestDTO req) {
        // Si @Valid falla, el GlobalExceptionHandler captura el error automáticamente.
        return ResponseEntity.ok(service.crearSolicitud(req));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, String>> cambiarEstado(
            @PathVariable int id,
            @RequestBody Map<String, String> body) {
        
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            // Retornamos un badRequest manual si falta el campo, o podríamos crear un DTO para esto
            return ResponseEntity.badRequest().body(Map.of("error", "Debe enviar el campo 'estado'"));
        }

        service.cambiarEstado(id, nuevoEstado);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Estado actualizado correctamente");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable int id) {
        service.eliminarSolicitud(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitud eliminada correctamente");
        return ResponseEntity.ok(response);
    }
}