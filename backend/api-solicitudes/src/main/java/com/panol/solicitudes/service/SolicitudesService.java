package com.panol.solicitudes.service;

import com.panol.solicitudes.dto.SolicitudRequestDTO;
import com.panol.solicitudes.dto.SolicitudResponseDTO;
import java.util.List;
import java.util.Map;

public interface SolicitudesService {
    List<SolicitudResponseDTO> listarSolicitudes();
    Map<String, Object> crearSolicitud(SolicitudRequestDTO req);
    void cambiarEstado(int id, String nuevoEstado);
    void eliminarSolicitud(int id);
}