package com.panol.inventario.controller;

import com.panol.inventario.dto.*;
import com.panol.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la API de inventario.
 * 
 * Expone operaciones CRUD para productos así como para las entidades
 * auxiliares de la aplicación (categorías, marcas y ubicaciones). Todas las
 * respuestas utilizan DTOs que extienden {@link org.springframework.hateoas.RepresentationModel}
 * para facilitar la incorporación de enlaces HATEOAS en futuras ampliaciones.
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    // ----- Endpoints de productos -----

    /**
     * Obtiene el listado de productos activos.
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarInventario());
    }

    /**
     * Obtiene un producto por su ID. Devuelve un mapa para mantener
     * compatibilidad con el frontend actual.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable int id) {
        return ResponseEntity.ok(service.obtenerProducto(id));
    }

    /**
     * Crea un nuevo producto en el inventario.
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> crear(@Valid @RequestBody ProductoRequestDTO req) {
        service.crearProducto(req);
        return ResponseEntity.ok(Map.of("message", "Producto creado correctamente"));
    }

    /**
     * Actualiza un producto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> editar(@PathVariable int id,
                                                     @Valid @RequestBody ProductoEditRequestDTO req) {
        service.editarProducto(id, req);
        return ResponseEntity.ok(Map.of("message", "Producto actualizado correctamente"));
    }

    /**
     * Elimina un producto (eliminación lógica).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable int id) {
        service.eliminarProducto(id);
        return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
    }

    // ----- Endpoints de categorías -----

    /**
     * Lista todas las categorías.
     */
    @GetMapping("/detalles/categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> categorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    /**
     * Obtiene una categoría en particular.
     */
    @GetMapping("/detalles/categorias/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoria(@PathVariable int id) {
        return ResponseEntity.ok(service.obtenerCategoria(id));
    }

    /**
     * Crea una nueva categoría.
     */
    @PostMapping("/detalles/categorias")
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.crearCategoria(dto));
    }

    /**
     * Actualiza una categoría existente.
     */
    @PutMapping("/detalles/categorias/{id}")
    public ResponseEntity<CategoriaResponseDTO> editarCategoria(@PathVariable int id,
                                                               @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarCategoria(id, dto));
    }

    /**
     * Elimina una categoría.
     */
    @DeleteMapping("/detalles/categorias/{id}")
    public ResponseEntity<Map<String, String>> eliminarCategoria(@PathVariable int id) {
        service.eliminarCategoria(id);
        return ResponseEntity.ok(Map.of("message", "Categoría eliminada correctamente"));
    }

    // ----- Endpoints de marcas -----

    /**
     * Lista todas las marcas.
     */
    @GetMapping("/detalles/marcas")
    public ResponseEntity<List<MarcaResponseDTO>> marcas() {
        return ResponseEntity.ok(service.listarMarcas());
    }

    /**
     * Obtiene una marca.
     */
    @GetMapping("/detalles/marcas/{id}")
    public ResponseEntity<MarcaResponseDTO> obtenerMarca(@PathVariable int id) {
        return ResponseEntity.ok(service.obtenerMarca(id));
    }

    /**
     * Crea una nueva marca.
     */
    @PostMapping("/detalles/marcas")
    public ResponseEntity<MarcaResponseDTO> crearMarca(@Valid @RequestBody MarcaRequestDTO dto) {
        return ResponseEntity.ok(service.crearMarca(dto));
    }

    /**
     * Actualiza una marca existente.
     */
    @PutMapping("/detalles/marcas/{id}")
    public ResponseEntity<MarcaResponseDTO> editarMarca(@PathVariable int id,
                                                       @Valid @RequestBody MarcaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarMarca(id, dto));
    }

    /**
     * Elimina una marca.
     */
    @DeleteMapping("/detalles/marcas/{id}")
    public ResponseEntity<Map<String, String>> eliminarMarca(@PathVariable int id) {
        service.eliminarMarca(id);
        return ResponseEntity.ok(Map.of("message", "Marca eliminada correctamente"));
    }

    // ----- Endpoints de ubicaciones -----

    /**
     * Lista todas las ubicaciones.
     */
    @GetMapping("/detalles/ubicaciones")
    public ResponseEntity<List<UbicacionResponseDTO>> ubicaciones() {
        return ResponseEntity.ok(service.listarUbicaciones());
    }

    /**
     * Obtiene una ubicación.
     */
    @GetMapping("/detalles/ubicaciones/{id}")
    public ResponseEntity<UbicacionResponseDTO> obtenerUbicacion(@PathVariable int id) {
        return ResponseEntity.ok(service.obtenerUbicacion(id));
    }

    /**
     * Crea una nueva ubicación.
     */
    @PostMapping("/detalles/ubicaciones")
    public ResponseEntity<UbicacionResponseDTO> crearUbicacion(@Valid @RequestBody UbicacionRequestDTO dto) {
        return ResponseEntity.ok(service.crearUbicacion(dto));
    }

    /**
     * Actualiza una ubicación existente.
     */
    @PutMapping("/detalles/ubicaciones/{id}")
    public ResponseEntity<UbicacionResponseDTO> editarUbicacion(@PathVariable int id,
                                                               @Valid @RequestBody UbicacionRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarUbicacion(id, dto));
    }

    /**
     * Elimina una ubicación.
     */
    @DeleteMapping("/detalles/ubicaciones/{id}")
    public ResponseEntity<Map<String, String>> eliminarUbicacion(@PathVariable int id) {
        service.eliminarUbicacion(id);
        return ResponseEntity.ok(Map.of("message", "Ubicación eliminada correctamente"));
    }
}