package com.panol.inventario.controller;

import com.panol.inventario.dto.*;
import com.panol.inventario.models.*;
import com.panol.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

  private final InventarioService service;

  public InventarioController(InventarioService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<ProductoResponseDTO>> listar() {
    return ResponseEntity.ok(service.listarInventario());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> obtener(@PathVariable int id) {
    return ResponseEntity.ok(service.obtenerProducto(id));
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> crear(@Valid @RequestBody ProductoRequestDTO req) {
    service.crearProducto(req);
    return ResponseEntity.ok(Map.of("message", "Producto creado correctamente"));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, String>> editar(@PathVariable int id,
      @Valid @RequestBody ProductoEditRequestDTO req) {
    service.editarProducto(id, req);
    return ResponseEntity.ok(Map.of("message", "Producto actualizado correctamente"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> eliminar(@PathVariable int id) {
    service.eliminarProducto(id);
    return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
  }

  // --- Endpoints Auxiliares ---

  @GetMapping("/detalles/categorias")
  public ResponseEntity<List<CategoriaProd>> categorias() {
    return ResponseEntity.ok(service.listarCategorias());
  }

  @GetMapping("/detalles/marcas")
  public ResponseEntity<List<Marca>> marcas() {
    return ResponseEntity.ok(service.listarMarcas());
  }

  @GetMapping("/detalles/ubicaciones")
  public ResponseEntity<List<Map<String, Object>>> ubicaciones() {
    return ResponseEntity.ok(service.listarUbicaciones());
  }

  @PostMapping("/detalles/categorias")
  public ResponseEntity<Map<String, Object>> crearCategoria(@RequestBody Map<String, String> body) {
    return ResponseEntity.ok(service.crearCategoria(body.get("nombre")));
  }

  @PostMapping("/detalles/marcas")
  public ResponseEntity<Map<String, Object>> crearMarca(@RequestBody Map<String, String> body) {
    return ResponseEntity.ok(service.crearMarca(body.get("nombre")));
  }

  @PostMapping("/detalles/ubicaciones")
  public ResponseEntity<Map<String, Object>> crearUbicacion(@RequestBody Map<String, Object> body) {
    return ResponseEntity.ok(service.crearUbicacion(
        (String) body.get("nombre_sala"),
        (String) body.get("estante"),
        Integer.valueOf(body.get("nivel").toString()),
        (String) body.get("descripcion")));
  }
}