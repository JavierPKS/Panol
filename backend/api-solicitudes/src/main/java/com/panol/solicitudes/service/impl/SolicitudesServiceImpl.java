package com.panol.solicitudes.service.impl;

import com.panol.solicitudes.dto.*;
import com.panol.solicitudes.entity.*;
import com.panol.solicitudes.exceptions.*;
import com.panol.solicitudes.repository.*;
import com.panol.solicitudes.service.SolicitudesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolicitudesServiceImpl implements SolicitudesService {

    private final SoliPrestamoRepository soliRepo;
    private final DetPrestamoRepository detRepo;

    public SolicitudesServiceImpl(SoliPrestamoRepository soliRepo, DetPrestamoRepository detRepo) {
        this.soliRepo = soliRepo;
        this.detRepo = detRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarSolicitudes() {
        return soliRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> crearSolicitud(SolicitudRequestDTO req) {
        // Crear cabecera de solicitud
        SoliPrestamo s = SoliPrestamo.builder()
                .rut(req.getRut())
                .motivo(req.getMotivo())
                .prioridad(req.getPrioridad())
                .fecha(LocalDate.now())
                .estado("pendiente")
                .build();
        SoliPrestamo guardado = soliRepo.save(s);

        // Crear detalles
        if (req.getProductos() != null && !req.getProductos().isEmpty()) {
            List<DetPrestamo> detalles = new ArrayList<>();
            for (DetalleSolicitudDTO item : req.getProductos()) {
                DetPrestamo d = DetPrestamo.builder()
                        .cantidad(item.getCantidad())
                        .idProducto(item.getIdProducto())
                        .fechaInicioPrestamo(item.getFechaInicio() != null ? item.getFechaInicio() : LocalDate.now())
                        .fechaRetornoPrestamo(item.getFechaRetorno() != null ? item.getFechaRetorno() : LocalDate.now().plusDays(3))
                        .solicitud(guardado)
                        .build();
                detalles.add(d);
            }
            detRepo.saveAll(detalles);
        }

        // Devolver mensaje y ID generado
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Solicitud creada correctamente");
        result.put("id_solicitud", guardado.getId());
        return result;
    }

    /** Mapea la entidad SoliPrestamo a SolicitudResponseDTO */
    private SolicitudResponseDTO mapToDto(SoliPrestamo s) {
        List<DetalleSolicitudDTO> detDtos = new ArrayList<>();
        if (s.getDetalles() != null) {
            detDtos = s.getDetalles().stream().map(d ->
                    DetalleSolicitudDTO.builder()
                            .idProducto(d.getIdProducto())
                            .cantidad(d.getCantidad())
                            .fechaInicio(d.getFechaInicioPrestamo())
                            .fechaRetorno(d.getFechaRetornoPrestamo())
                            .build()
            ).collect(Collectors.toList());
        }
        return SolicitudResponseDTO.builder()
                .id(s.getId())
                .rut(s.getRut())
                .motivo(s.getMotivo())
                .prioridad(s.getPrioridad())
                .estado(s.getEstado())
                .fechaSolicitud(s.getFecha())
                .detalles(detDtos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponseDTO obtenerSolicitudPorId(int id) {
        SoliPrestamo solicitud = soliRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada con ID: " + id));
        return mapToDto(solicitud);
    }

    @Override
    @Transactional
    public SolicitudResponseDTO actualizarSolicitud(int id, SolicitudRequestDTO req) {
        // Buscar solicitud
        SoliPrestamo solicitud = soliRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada con ID: " + id));

        // Actualizar campos básicos
        solicitud.setRut(req.getRut());
        solicitud.setMotivo(req.getMotivo());
        solicitud.setPrioridad(req.getPrioridad());

        // Eliminar detalles existentes
        if (solicitud.getDetalles() != null && !solicitud.getDetalles().isEmpty()) {
            detRepo.deleteAll(solicitud.getDetalles());
            solicitud.getDetalles().clear();
        }

        // Crear nuevos detalles
        if (req.getProductos() != null && !req.getProductos().isEmpty()) {
            List<DetPrestamo> nuevos = new ArrayList<>();
            for (DetalleSolicitudDTO item : req.getProductos()) {
                DetPrestamo d = DetPrestamo.builder()
                        .cantidad(item.getCantidad())
                        .idProducto(item.getIdProducto())
                        .fechaInicioPrestamo(item.getFechaInicio() != null ? item.getFechaInicio() : LocalDate.now())
                        .fechaRetornoPrestamo(item.getFechaRetorno() != null ? item.getFechaRetorno() : LocalDate.now().plusDays(3))
                        .solicitud(solicitud)
                        .build();
                nuevos.add(d);
            }
            solicitud.setDetalles(nuevos);
        }

        // Guardar cambios y devolver DTO
        SoliPrestamo actualizada = soliRepo.save(solicitud);
        return mapToDto(actualizada);
    }

    @Override
    @Transactional
    public DetalleSolicitudDTO actualizarDetalle(int solicitudId, int detalleId, DetalleSolicitudDTO dto) {
        DetPrestamo detalle = detRepo.findById(detalleId)
                .orElseThrow(() -> new NotFoundException("Detalle no encontrado con ID: " + detalleId));
        if (detalle.getSolicitud() == null || !detalle.getSolicitud().getId().equals(solicitudId)) {
            throw new BadRequestException("El detalle no pertenece a la solicitud especificada");
        }
        // Actualizar campos indicados
        if (dto.getCantidad() != null) {
            detalle.setCantidad(dto.getCantidad());
        }
        if (dto.getIdProducto() != null) {
            detalle.setIdProducto(dto.getIdProducto());
        }
        if (dto.getFechaInicio() != null) {
            detalle.setFechaInicioPrestamo(dto.getFechaInicio());
        }
        if (dto.getFechaRetorno() != null) {
            detalle.setFechaRetornoPrestamo(dto.getFechaRetorno());
        }
        DetPrestamo actualizado = detRepo.save(detalle);
        return DetalleSolicitudDTO.builder()
                .idProducto(actualizado.getIdProducto())
                .cantidad(actualizado.getCantidad())
                .fechaInicio(actualizado.getFechaInicioPrestamo())
                .fechaRetorno(actualizado.getFechaRetornoPrestamo())
                .build();
    }

    @Override
    @Transactional
    public void eliminarDetalle(int solicitudId, int detalleId) {
        DetPrestamo detalle = detRepo.findById(detalleId)
                .orElseThrow(() -> new NotFoundException("Detalle no encontrado con ID: " + detalleId));
        if (detalle.getSolicitud() == null || !detalle.getSolicitud().getId().equals(solicitudId)) {
            throw new BadRequestException("El detalle no pertenece a la solicitud especificada");
        }
        detRepo.delete(detalle);
    }

    @Override
    @Transactional
    public void cambiarEstado(int id, String nuevoEstado) {
        SoliPrestamo s = soliRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada con ID: " + id));
        // Validar estados permitidos
        if (nuevoEstado == null ||
            (!nuevoEstado.equalsIgnoreCase("aprobado") &&
             !nuevoEstado.equalsIgnoreCase("pendiente") &&
             !nuevoEstado.equalsIgnoreCase("rechazado"))) {
            throw new BadRequestException("Estado inválido. Valores permitidos: aprobado, pendiente, rechazado");
        }
        s.setEstado(nuevoEstado.toLowerCase());
        soliRepo.save(s);
    }

    @Override
    @Transactional
    public void eliminarSolicitud(int id) {
        if (!soliRepo.existsById(id)) {
            throw new NotFoundException("Solicitud no encontrada para eliminar");
        }
        soliRepo.deleteById(id);
    }
}
