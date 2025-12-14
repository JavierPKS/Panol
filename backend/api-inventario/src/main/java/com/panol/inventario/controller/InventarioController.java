package com.panol.inventario.controller;

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

  @GetMapping
  public List<ProductoResponse> listar() {
    return service.listarInventario();
  }

  @GetMapping("/{id}")
  public Map<String,Object> obtener(@PathVariable int id) {
    return service.obtenerProducto(id);
  }

  @PostMapping
  public Map<String,String> crear(@Valid @RequestBody ProductoRequest req) {
    service.crearProducto(req);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Producto creado correctamente");
    return result;
  }

  @PutMapping("/{id}")
  public Map<String,String> editar(@PathVariable int id, @Valid @RequestBody ProductoEditRequest req) {
    service.editarProducto(id, req);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Producto actualizado");
    return result;
  }

  @DeleteMapping("/{id}")
  public Map<String,String> eliminar(@PathVariable int id) {
    service.eliminarProducto(id);
    Map<String,String> result = new HashMap<>();
    result.put("message", "Producto eliminado");
    return result;
  }

  @GetMapping("/detalles/categorias")
  public List<CategoriaProd> categorias() {
    return service.listarCategorias();
  }

  @GetMapping("/detalles/marcas")
  public List<Marca> marcas() {
    return service.listarMarcas();
  }

  @GetMapping("/detalles/ubicaciones")
  public List<Map<String,Object>> ubicaciones() {
    return service.listarUbicaciones();
  }

  @PostMapping("/detalles/categorias")
  public Map<String,Object> crearCategoria(@RequestBody Map<String,String> body) {
    return service.crearCategoria(body.get("nombre"));
  }

  @PostMapping("/detalles/marcas")
  public Map<String,Object> crearMarca(@RequestBody Map<String,String> body) {
    return service.crearMarca(body.get("nombre"));
  }

  @PostMapping("/detalles/ubicaciones")
  public Map<String,Object> crearUbicacion(@RequestBody Map<String,Object> body) {
    String sala = (String) body.get("nombre_sala");
    String estante = (String) body.get("estante");
    Integer nivel = Integer.valueOf(body.get("nivel").toString());
    String desc = body.get("descripcion") == null ? null : body.get("descripcion").toString();
    return service.crearUbicacion(sala, estante, nivel, desc);
  }
}

