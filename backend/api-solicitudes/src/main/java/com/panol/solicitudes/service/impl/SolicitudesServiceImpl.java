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
            
            // Construimos la lista de detalles
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

            // Construimos el DTO principal
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
        
        // 1. Crear cabecera (Solicitud)
        SoliPrestamo s = SoliPrestamo.builder()
                .rut(req.getRut())
                .motivo(req.getMotivo())
                .prioridad(req.getPrioridad())
                .fecha(LocalDate.now())
                .estado("pendiente") // Estado inicial por defecto
                .build();
        
        // Guardar primero para obtener el ID
        SoliPrestamo guardado = soliRepo.save(s);

        // 2. Procesar detalles
        if (req.getProductos() != null && !req.getProductos().isEmpty()) {
            List<DetPrestamo> detalles = new ArrayList<>();
            
            for (DetalleSolicitudDTO item : req.getProductos()) {
                DetPrestamo d = DetPrestamo.builder()
                        .cantidad(item.getCantidad())
                        .idProducto(item.getIdProducto())
                        // Usar fecha actual si viene nula
                        .fechaInicioPrestamo(item.getFechaInicio() != null ? item.getFechaInicio() : LocalDate.now())
                        // Retorno por defecto a 3 días si viene nulo
                        .fechaRetornoPrestamo(item.getFechaRetorno() != null ? item.getFechaRetorno() : LocalDate.now().plusDays(3))
                        .solicitud(guardado) // Asignar la relación FK
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

        // Validación simple de estados permitidos
        if (nuevoEstado == null || (
            !nuevoEstado.equalsIgnoreCase("aprobado") && 
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