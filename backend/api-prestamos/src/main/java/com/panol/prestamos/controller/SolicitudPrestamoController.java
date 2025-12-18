package com.panol.prestamos.controller;

import com.panol.prestamos.dto.*;
import com.panol.prestamos.entity.*;
import com.panol.prestamos.service.*;

import jakarta.transaction.Transactional;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/prestamos")
public class SolicitudPrestamoController {
        private final SolicitudPrestamoService solicitudService;
        private final DetallePrestamoService detalleService;

        public SolicitudPrestamoController(
                SolicitudPrestamoService solicitudService,
                DetallePrestamoService detalleService) {
                this.solicitudService = solicitudService;
                this.detalleService = detalleService;
        }

        @Transactional
        @PostMapping
        public ResponseEntity<SolicitudPrestamoDTO> crearPrestamo(
                @RequestBody CrearSolicitudPrestamoDTO request) {

                SolicitudPrestamo solicitud = SolicitudPrestamo.builder()
                        .usuarioRut(request.getUsuarioRut())
                        .motivoPrestamo(request.getMotivoPrestamo())
                        .prioridad(request.getPrioridad())
                        .estadoSolicitud("pendiente")
                        .fechaSolicitud(LocalDate.now())
                        .build();

                SolicitudPrestamo guardada = solicitudService.crear(solicitud);

                request.getDetalles().forEach(d -> {
                DetallePrestamo detalle = DetallePrestamo.builder()
                        .productoIdPrincipal(d.getProductoIdPrincipal())
                        .cantidad(d.getCantidad())
                        .fechaIncioPrestamo(LocalDate.now())
                        .fechaRetornoPrestamo(LocalDate.now().plusDays(7))
                        .solicitudPrestamoId(guardada.getIdPrestamo())
                        .build();
                detalleService.guardar(detalle);
                });

                SolicitudPrestamoDTO dto = mapToDTO(guardada);
                dto.add(linkTo(methodOn(SolicitudPrestamoController.class)
                        .buscarPorId(guardada.getIdPrestamo())).withSelfRel());

                return ResponseEntity.ok(dto);
        }

        @GetMapping
        public ResponseEntity<CollectionModel<SolicitudPrestamoDTO>> listar() {

                List<SolicitudPrestamoDTO> dtos = solicitudService.listar()
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(
                        CollectionModel.of(dtos,
                                linkTo(methodOn(SolicitudPrestamoController.class)
                                        .listar()).withSelfRel())
                );
        }

        @GetMapping("/{id}")
        public ResponseEntity<SolicitudPrestamoDTO> buscarPorId(
                @PathVariable Long id) {

                return solicitudService.buscarPorId(id)
                        .map(solicitud -> {
                        SolicitudPrestamoDTO dto = mapToDTO(solicitud);
                        dto.add(linkTo(methodOn(SolicitudPrestamoController.class)
                                .listar()).withRel("prestamos"));
                        return ResponseEntity.ok(dto);
                        })
                        .orElse(ResponseEntity.notFound().build());
        }

        private SolicitudPrestamoDTO mapToDTO(SolicitudPrestamo s) {
                return SolicitudPrestamoDTO.builder()
                        .idPrestamo(s.getIdPrestamo())
                        .estadoSolicitud(s.getEstadoSolicitud())
                        .usuarioRut(s.getUsuarioRut())
                        .fechaSolicitud(s.getFechaSolicitud())
                        .motivoPrestamo(s.getMotivoPrestamo())
                        .prioridad(s.getPrioridad())
                        .build();
        }

        @GetMapping("/{id}/detalles")
        public ResponseEntity<List<DetallePrestamoDTO>> verDetalles(
                @PathVariable Long id) {

        List<DetallePrestamoDTO> detalles = detalleService
                .buscarPorSolicitud(id)
                .stream()
                .map(d -> DetallePrestamoDTO.builder()
                        .idDetalle(d.getIdDetalle())
                        .productoIdPrincipal(d.getProductoIdPrincipal())
                        .cantidad(d.getCantidad())
                        .fechaIncioPrestamo(d.getFechaIncioPrestamo())
                        .fechaRetornoPrestamo(d.getFechaRetornoPrestamo())
                        .fechaDevolucionPrestamo(d.getFechaDevolucionPrestamo())
                        .build())
                .toList();

        return ResponseEntity.ok(detalles);
        }

        @PutMapping("/{id}/devolver")
        public ResponseEntity<Void> devolverPrestamo(@PathVariable Long id) {

        detalleService.registrarDevolucion(id);

        return ResponseEntity.ok().build();
        }
}
