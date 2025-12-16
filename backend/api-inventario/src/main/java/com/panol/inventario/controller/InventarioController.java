package com.panol.inventario.controller;

import com.panol.inventario.exceptions.NotFoundException;
import com.panol.inventario.exceptions.BadRequestException;

import com.panol.inventario.dto.*;
import com.panol.inventario.models.*;
import com.panol.inventario.service.InventarioService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

  private final InventarioService service;

  public InventarioController(InventarioService service) {
    this.service = service;
  }

  // =========================
  // LISTAR INVENTARIO
  // =========================
  @GetMapping
  public List<ProductoResponseDTO> listar() {
    return service.listarInventario();
  }

  // =========================
  // OBTENER PRODUCTO
  // =========================
  @GetMapping("/{id}")
  public Map<String, Object> obtener(@PathVariable int id) {

    Map<String, Object> producto = service.obtenerProducto(id);

    if (producto == null || producto.isEmpty()) {
      throw new NotFoundException("Producto no encontrado");
    }

    return producto;
  }

  // =========================
  // CREAR PRODUCTO
  // =========================
  @PostMapping
  public Map<String, String> crear(@Valid @RequestBody ProductoRequestDTO req) {

    if (req.getCantidad() < 0) {
      throw new BadRequestException("El stock no puede ser negativo");
    }

    service.crearProducto(req);

    Map<String, String> result = new HashMap<>();
    result.put("message", "Producto creado correctamente");
    return result;
  }

  // =========================
  // EDITAR PRODUCTO
  // =========================
  @PutMapping("/{id}")
  public Map<String, String> editar(
      @PathVariable int id,
      @Valid @RequestBody ProductoEditRequestDTO req) {

    if (!service.existeProducto(id)) {
      throw new NotFoundException("Producto no encontrado");
    }

    if (req.getCantidad() < 0) {
      throw new BadRequestException("El stock no puede ser negativo");
    }

    service.editarProducto(id, req);

    Map<String, String> result = new HashMap<>();
    result.put("message", "Producto actualizado");
    return result;
  }

  // =========================
  // ELIMINAR PRODUCTO
  // =========================
  @DeleteMapping("/{id}")
  public Map<String, String> eliminar(@PathVariable int id) {

    if (!service.existeProducto(id)) {
      throw new NotFoundException("Producto no encontrado");
    }

    service.eliminarProducto(id);

    Map<String, String> result = new HashMap<>();
    result.put("message", "Producto eliminado");
    return result;
  }

  // =========================
  // DETALLES
  // =========================
  @GetMapping("/detalles/categorias")
  public List<CategoriaProd> categorias() {
    return service.listarCategorias();
  }

  @GetMapping("/detalles/marcas")
  public List<Marca> marcas() {
    return service.listarMarcas();
  }

  @GetMapping("/detalles/ubicaciones")
  public List<Map<String, Object>> ubicaciones() {
    return service.listarUbicaciones();
  }

  // =========================
  // CREAR CATEGORIA
  // =========================
  @PostMapping("/detalles/categorias")
  public Map<String, Object> crearCategoria(@RequestBody Map<String, String> body) {

    if (body.get("nombre") == null || body.get("nombre").isBlank()) {
      throw new BadRequestException("El nombre de la categoría es obligatorio");
    }

    return service.crearCategoria(body.get("nombre"));
  }

  // =========================
  // CREAR MARCA
  // =========================
  @PostMapping("/detalles/marcas")
  public Map<String, Object> crearMarca(@RequestBody Map<String, String> body) {

    if (body.get("nombre") == null || body.get("nombre").isBlank()) {
      throw new BadRequestException("El nombre de la marca es obligatorio");
    }

    return service.crearMarca(body.get("nombre"));
  }

  // =========================
  // CREAR UBICACION
  // =========================
  @PostMapping("/detalles/ubicaciones")
  public Map<String, Object> crearUbicacion(@RequestBody Map<String, Object> body) {

    String sala = (String) body.get("nombre_sala");
    String estante = (String) body.get("estante");
    Object nivelObj = body.get("nivel");

    if (sala == null || sala.isBlank() || estante == null || estante.isBlank() || nivelObj == null) {
      throw new BadRequestException("Sala, estante y nivel son obligatorios");
    }

    Integer nivel = Integer.valueOf(nivelObj.toString());
    String desc = body.get("descripcion") == null ? null : body.get("descripcion").toString();

    return service.crearUbicacion(sala, estante, nivel, desc);
  }
}
