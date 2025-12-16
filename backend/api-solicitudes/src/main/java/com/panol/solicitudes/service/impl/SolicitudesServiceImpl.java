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
    public List<SolicitudResponseDTO> listarSolicitudes() {
        return soliRepo.findAll().stream().map(s -> {
            SolicitudResponseDTO dto = new SolicitudResponseDTO();
            dto.setId(s.getId());
            dto.setRut(s.getRut());
            dto.setMotivo(s.getMotivo());
            dto.setPrioridad(s.getPrioridad());
            dto.setEstado(s.getEstado());
            dto.setFechaSolicitud(s.getFecha());

            if (s.getDetalles() != null) {
                List<DetalleSolicitudDTO> detDtos = s.getDetalles().stream().map(d -> {
                    DetalleSolicitudDTO dd = new DetalleSolicitudDTO();
                    dd.setIdProducto(d.getIdProducto());
                    dd.setCantidad(d.getCantidad());
                    dd.setFechaInicio(d.getFechaInicio());
                    dd.setFechaRetorno(d.getFechaRetorno());
                    return dd;
                }).collect(Collectors.toList());
                dto.setDetalles(detDtos);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> crearSolicitud(SolicitudRequestDTO req) {
        
        // Creamos la cabecera
        SoliPrestamo s = new SoliPrestamo();
        s.setRut(req.getRut());
        s.setMotivo(req.getMotivo());
        s.setPrioridad(req.getPrioridad());
        s.setFecha(LocalDate.now());
        s.setEstado("pendiente");
        
        // Guardamos primero para obtener el ID
        SoliPrestamo guardado = soliRepo.save(s);

        // Procesamos los detalles
        if (req.getProductos() != null && !req.getProductos().isEmpty()) {
            List<DetPrestamo> detalles = new ArrayList<>();
            for (DetalleSolicitudDTO item : req.getProductos()) {
                DetPrestamo d = new DetPrestamo();
                d.setCantidad(item.getCantidad());
                d.setIdProducto(item.getIdProducto());
                // Validamos fechas (o usamos defaults)
                d.setFechaInicio(item.getFechaInicio() != null ? item.getFechaInicio() : LocalDate.now());
                d.setFechaRetorno(item.getFechaRetorno() != null ? item.getFechaRetorno() : LocalDate.now().plusDays(3));
                
                // Asignamos la relación
                d.setSolicitud(guardado);
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
        if (!nuevoEstado.equalsIgnoreCase("aprobado") && 
            !nuevoEstado.equalsIgnoreCase("pendiente") && 
            !nuevoEstado.equalsIgnoreCase("rechazado")) {
            throw new BadRequestException("Estado inválido. Valores permitidos: aprobado, pendiente, rechazado");
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