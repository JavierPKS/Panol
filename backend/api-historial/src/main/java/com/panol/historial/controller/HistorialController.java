package com.panol.historial.controller;

import com.panol.historial.dto.HistorialCreateRequestDTO;
import com.panol.historial.dto.HistorialRow;
import com.panol.historial.service.HistorialService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador REST para exponer operaciones sobre el historial de préstamos.
 */
@RestController
@RequestMapping("/api/historial")
@CrossOrigin(origins = "*")
public class HistorialController {

  private final HistorialService service;

  public HistorialController(HistorialService service) {
    this.service = service;
  }

  /** Lista todos los registros del historial con enlaces HATEOAS. */
  @GetMapping
  public ResponseEntity<CollectionModel<HistorialRow>> listar() {
    List<HistorialRow> historial = service.listarHistorial();
    historial.forEach(dto -> {
      dto.add(linkTo(methodOn(HistorialController.class)
          .buscar(dto.getIdDetalle())).withSelfRel());
      dto.add(linkTo(methodOn(HistorialController.class)
          .editar(dto.getIdDetalle(), dto)).withRel("editar"));
      dto.add(linkTo(methodOn(HistorialController.class)
          .eliminar(dto.getIdDetalle())).withRel("eliminar"));
    });
    CollectionModel<HistorialRow> collection = CollectionModel.of(historial);
    collection.add(linkTo(methodOn(HistorialController.class)
        .listar()).withSelfRel());
    return ResponseEntity.ok(collection);
  }

  @PostMapping
  public ResponseEntity<HistorialRow> crear(
      @RequestBody HistorialCreateRequestDTO dto) {

    HistorialRow creado = service.crearHistorial(dto);

    creado.add(linkTo(methodOn(HistorialController.class)
        .buscar(creado.getIdDetalle())).withSelfRel());

    creado.add(linkTo(methodOn(HistorialController.class)
        .listar()).withRel(IanaLinkRelations.COLLECTION));

    return ResponseEntity.status(201).body(creado);
  }

  /**
   * Obtiene un registro del historial por su ID con enlaces a colección y
   * operaciones.
   */
  @GetMapping("/{id}")
  public ResponseEntity<HistorialRow> buscar(@PathVariable("id") int idDetalle) {
    HistorialRow dto = service.buscarPorId(idDetalle);
    dto.add(linkTo(methodOn(HistorialController.class)
        .buscar(idDetalle)).withSelfRel());
    dto.add(linkTo(methodOn(HistorialController.class)
        .listar()).withRel(IanaLinkRelations.COLLECTION));
    dto.add(linkTo(methodOn(HistorialController.class)
        .editar(idDetalle, dto)).withRel("editar"));
    dto.add(linkTo(methodOn(HistorialController.class)
        .eliminar(idDetalle)).withRel("eliminar"));
    return ResponseEntity.ok(dto);
  }

  /**
   * Edita un registro de historial y devuelve el recurso actualizado con enlaces.
   */
  @PutMapping("/{id}")
  public ResponseEntity<HistorialRow> editar(@PathVariable("id") int idDetalle,
      @RequestBody HistorialRow dto) {
    HistorialRow actualizado = service.editarHistorial(idDetalle, dto);
    actualizado.add(linkTo(methodOn(HistorialController.class)
        .buscar(actualizado.getIdDetalle())).withSelfRel());
    actualizado.add(linkTo(methodOn(HistorialController.class)
        .listar()).withRel(IanaLinkRelations.COLLECTION));
    return ResponseEntity.ok(actualizado);
  }

  /** Elimina un registro de historial y devuelve un mensaje de confirmación. */
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> eliminar(@PathVariable("id") int idDetalle) {
    service.eliminarHistorial(idDetalle);
    return ResponseEntity.ok(Map.of("message", "Registro de historial eliminado correctamente"));
  }
}
