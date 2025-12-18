package com.panol.solicitudes.controller;

import com.panol.solicitudes.dto.DetalleSolicitudDTO;
import com.panol.solicitudes.dto.SolicitudRequestDTO;
import com.panol.solicitudes.dto.SolicitudResponseDTO;
import com.panol.solicitudes.service.SolicitudesService;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.IanaLinkRelations;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudesController {

    private final SolicitudesService service;

    public SolicitudesController(SolicitudesService service) {
        this.service = service;
    }

    /** Listado con enlaces HATEOAS */
    @GetMapping
    public ResponseEntity<CollectionModel<SolicitudResponseDTO>> listar() {
        List<SolicitudResponseDTO> solicitudes = service.listarSolicitudes();
        solicitudes.forEach(dto -> {
            dto.add(linkTo(methodOn(SolicitudesController.class)
                    .obtenerPorId(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(SolicitudesController.class)
                    .actualizar(dto.getId(), (SolicitudRequestDTO) null)).withRel("editar"));
            dto.add(linkTo(methodOn(SolicitudesController.class)
                    .eliminar(dto.getId())).withRel("eliminar"));
            dto.add(linkTo(methodOn(SolicitudesController.class)
                    .cambiarEstado(dto.getId(), Map.of("estado",""))).withRel("cambiarEstado"));
        });
        CollectionModel<SolicitudResponseDTO> collection = CollectionModel.of(solicitudes);
        collection.add(linkTo(methodOn(SolicitudesController.class).listar()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    /** Crea una solicitud y devuelve el recurso creado con enlaces */
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crear(@Valid @RequestBody SolicitudRequestDTO req) {
        Map<String, Object> resultado = service.crearSolicitud(req);
        Integer id = (Integer) resultado.get("id_solicitud");
        SolicitudResponseDTO dto = service.obtenerSolicitudPorId(id);
        dto.add(linkTo(methodOn(SolicitudesController.class).obtenerPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(SolicitudesController.class).listar()).withRel(IanaLinkRelations.COLLECTION));
        return ResponseEntity.ok(dto);
    }

    /** Devuelve una solicitud por ID con enlaces */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> obtenerPorId(@PathVariable int id) {
        SolicitudResponseDTO dto = service.obtenerSolicitudPorId(id);
        dto.add(linkTo(methodOn(SolicitudesController.class).obtenerPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(SolicitudesController.class).listar()).withRel(IanaLinkRelations.COLLECTION));
        dto.add(linkTo(methodOn(SolicitudesController.class).actualizar(id, (SolicitudRequestDTO) null)).withRel("editar"));
        dto.add(linkTo(methodOn(SolicitudesController.class).eliminar(id)).withRel("eliminar"));
        dto.add(linkTo(methodOn(SolicitudesController.class).cambiarEstado(id, Map.of("estado",""))).withRel("cambiarEstado"));
        return ResponseEntity.ok(dto);
    }

    /** Actualiza la cabecera y los detalles de una solicitud */
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> actualizar(@PathVariable int id,
                                                           @RequestBody SolicitudRequestDTO req) {
        SolicitudResponseDTO actualizado = service.actualizarSolicitud(id, req);
        actualizado.add(linkTo(methodOn(SolicitudesController.class).obtenerPorId(id)).withSelfRel());
        actualizado.add(linkTo(methodOn(SolicitudesController.class).listar()).withRel(IanaLinkRelations.COLLECTION));
        actualizado.add(linkTo(methodOn(SolicitudesController.class).eliminar(id)).withRel("eliminar"));
        actualizado.add(linkTo(methodOn(SolicitudesController.class).cambiarEstado(id, Map.of("estado",""))).withRel("cambiarEstado"));
        return ResponseEntity.ok(actualizado);
    }

    /** Cambia el estado de una solicitud */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String,String>> cambiarEstado(@PathVariable int id,
                                                             @RequestBody Map<String,String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error","Debe enviar el campo 'estado'"));
        }
        service.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(Map.of("message","Estado actualizado correctamente"));
    }

    /** Elimina una solicitud completa */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> eliminar(@PathVariable int id) {
        service.eliminarSolicitud(id);
        return ResponseEntity.ok(Map.of("message","Solicitud eliminada correctamente"));
    }

    /** Actualiza un detalle puntual */
    @PutMapping("/{id}/detalles/{detalleId}")
    public ResponseEntity<DetalleSolicitudDTO> actualizarDetalle(@PathVariable int id,
                                                                 @PathVariable int detalleId,
                                                                 @RequestBody DetalleSolicitudDTO detalleDto) {
        DetalleSolicitudDTO actualizado = service.actualizarDetalle(id, detalleId, detalleDto);
        actualizado.add(linkTo(methodOn(SolicitudesController.class).obtenerPorId(id)).withRel("solicitud"));
        actualizado.add(linkTo(methodOn(SolicitudesController.class)
                .actualizarDetalle(id, detalleId, (DetalleSolicitudDTO) null)).withSelfRel());
        actualizado.add(linkTo(methodOn(SolicitudesController.class).eliminarDetalle(id, detalleId)).withRel("eliminar"));
        return ResponseEntity.ok(actualizado);
    }

    /** Elimina un detalle puntual */
    @DeleteMapping("/{id}/detalles/{detalleId}")
    public ResponseEntity<Map<String,String>> eliminarDetalle(@PathVariable int id,
                                                               @PathVariable int detalleId) {
        service.eliminarDetalle(id, detalleId);
        return ResponseEntity.ok(Map.of("message","Detalle eliminado correctamente"));
    }
}
