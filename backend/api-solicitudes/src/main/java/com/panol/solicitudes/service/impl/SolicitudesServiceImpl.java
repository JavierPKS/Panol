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
        return soliRepo.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Object> crearSolicitud(SolicitudRequestDTO req) {
        
        // 1. Crear cabecera de la solicitud
        SoliPrestamo solicitud = new SoliPrestamo();
        // Nota: Lombok genera los getters con el nombre exacto de la variable snake_case en algunos casos o camelCase standard
        // pero al usar @Data con variables snake_case, el getter suele ser getRut_usuario()
        solicitud.setRut(req.getRut_usuario());
        solicitud.setMotivo(req.getMotivo_prestamo());
        solicitud.setPrioridad(req.getPrioridad());
        solicitud.setFecha(LocalDate.now());
        solicitud.setEstado("pendiente");
        
        SoliPrestamo solicitudGuardada = soliRepo.save(solicitud);

        // 2. Procesar detalles
        if (req.getProductos() != null && !req.getProductos().isEmpty()) {
            List<DetPrestamo> detallesEntidad = new ArrayList<>();
            
            for (DetalleSolicitudDTO item : req.getProductos()) {
                DetPrestamo det = new DetPrestamo();
                det.setCantidad(item.getCantidad());
                det.setIdProducto(item.getId_producto());
                
                det.setFechaInicio(item.getFecha_inicio() != null ? item.getFecha_inicio() : LocalDate.now());
                det.setFechaRetorno(item.getFecha_retorno() != null ? item.getFecha_retorno() : LocalDate.now().plusDays(3));
                
                det.setSolicitud(solicitudGuardada);
                detallesEntidad.add(det);
            }
            detRepo.saveAll(detallesEntidad);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Solicitud creada correctamente");
        response.put("id_solicitud", solicitudGuardada.getId());
        return response;
    }

    @Override
    @Transactional
    public void cambiarEstado(int id, String nuevoEstado) {
        SoliPrestamo s = soliRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada con ID: " + id));

        // Validación simple
        if (!nuevoEstado.equalsIgnoreCase("aprobado") && 
            !nuevoEstado.equalsIgnoreCase("pendiente") &&
            !nuevoEstado.equalsIgnoreCase("rechazado")) { 
            // Manejar error o ignorar según lógica de negocio
        }

        s.setEstado(nuevoEstado);
        soliRepo.save(s);
    }

    @Override
    @Transactional
    public void eliminarSolicitud(int id) {
        if (!soliRepo.existsById(id)) {
            throw new NotFoundException("Solicitud no encontrada");
        }
        soliRepo.deleteById(id);
    }

    // Mapeador auxiliar actualizado a los nuevos DTOs
    private SolicitudResponseDTO mapearADTO(SoliPrestamo s) {
        List<DetalleSolicitudDTO> detallesDTO = null;
        
        if (s.getDetalles() != null) {
            detallesDTO = s.getDetalles().stream().map(d -> 
                DetalleSolicitudDTO.builder()
                    .id_producto(d.getIdProducto())
                    .cantidad(d.getCantidad())
                    .fecha_inicio(d.getFechaInicio())
                    .fecha_retorno(d.getFechaRetorno())
                    .build()
            ).collect(Collectors.toList());
        }

        return SolicitudResponseDTO.builder()
                .id_solicitud(s.getId())
                .rut_usuario(s.getRut())
                .motivo_prestamo(s.getMotivo())
                .prioridad(s.getPrioridad())
                .estado_solicitud(s.getEstado())
                .fecha_solicitud(s.getFecha())
                .detalle_productos(detallesDTO)
                .build();
    }
}