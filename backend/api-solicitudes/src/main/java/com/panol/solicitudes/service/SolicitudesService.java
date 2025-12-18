package com.panol.solicitudes.service;

import com.panol.solicitudes.dto.DetalleSolicitudDTO;
import com.panol.solicitudes.dto.SolicitudRequestDTO;
import com.panol.solicitudes.dto.SolicitudResponseDTO;
import java.util.List;
import java.util.Map;

public interface SolicitudesService {
    List<SolicitudResponseDTO> listarSolicitudes();
    Map<String, Object> crearSolicitud(SolicitudRequestDTO req);
    void cambiarEstado(int id, String nuevoEstado);
    void eliminarSolicitud(int id);

    //obtener por ID
    SolicitudResponseDTO obtenerSolicitudPorId(int id);
    // actualización completa (cuerpo y detalles)
    SolicitudResponseDTO actualizarSolicitud(int id, SolicitudRequestDTO req);
    // actualizar un detalle puntual
    DetalleSolicitudDTO actualizarDetalle(int solicitudId, int detalleId, DetalleSolicitudDTO dto);
    // eliminar un detalle puntual
    void eliminarDetalle(int solicitudId, int detalleId);
}
