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
        return soliRepo.findAll().stream().map(s -> {
            
            // 1. Construimos la lista de detalles usando Builder
            List<DetalleSolicitudDTO> detDtos = null;
            if (s.getDetalles() != null) {
                detDtos = s.getDetalles().stream().map(d -> 
                    DetalleSolicitudDTO.builder()
                        .idProducto(d.getIdProducto())
                        .cantidad(d.getCantidad())
                        .fechaInicio(d.getFechaInicioPrestamo()) // Recordar corrección de nombre columna
                        .fechaRetorno(d.getFechaRetornoPrestamo())
                        .build()
                ).collect(Collectors.toList());
            }

            // 2. Construimos el DTO principal usando Builder
            return SolicitudResponseDTO.builder()
                    .id(s.getId())
                    .rut(s.getRut())
                    .motivo(s.getMotivo())
                    .prioridad(s.getPrioridad())
                    .estado(s.getEstado())
                    .fechaSolicitud(s.getFecha())
                    .detalles(detDtos)
                    .build();

        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> crearSolicitud(SolicitudRequestDTO req) {
        
        // 1. Crear cabecera (Solicitud) usando Builder
        SoliPrestamo s = SoliPrestamo.builder()
                .rut(req.getRut())
                .motivo(req.getMotivo())
                .prioridad(req.getPrioridad())
                .fecha(LocalDate.now())
                .estado("pendiente")
                .build();
        
        // Guardar para generar ID
        SoliPrestamo guardado = soliRepo.save(s);

        // 2. Procesar detalles
        if (req.getProductos() != null && !req.getProductos().isEmpty()) {
            List<DetPrestamo> detalles = new ArrayList<>();
            
            for (DetalleSolicitudDTO item : req.getProductos()) {
                // Usamos Builder para crear la entidad de detalle
                DetPrestamo d = DetPrestamo.builder()
                        .cantidad(item.getCantidad())
                        .idProducto(item.getIdProducto())
                        .fechaInicioPrestamo(item.getFechaInicio() != null ? item.getFechaInicio() : LocalDate.now())
                        .fechaRetornoPrestamo(item.getFechaRetorno() != null ? item.getFechaRetorno() : LocalDate.now().plusDays(3))
                        .solicitud(guardado) // Asignamos la relación
                        .build();
                
                detalles.add(d);
            }
            detRepo.saveAll(detalles);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Solicitud creada correctamente");
        result.put("id_solicitud", guardado.getId());
        return result;
    }

    @Override
    @Transactional
    public void cambiarEstado(int id, String nuevoEstado) {
        SoliPrestamo s = soliRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada con ID: " + id));

        if (!nuevoEstado.equalsIgnoreCase("aprobado") && 
            !nuevoEstado.equalsIgnoreCase("pendiente") && 
            !nuevoEstado.equalsIgnoreCase("rechazado")) {
            throw new BadRequestException("Estado inválido. Valores: aprobado, pendiente, rechazado");
        }

        s.setEstado(nuevoEstado);
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