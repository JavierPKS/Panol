package com.panol.solicitudes.service;

import com.panol.solicitudes.dto.*;
import java.util.List;
import java.util.Map;

public interface SolicitudesService {
    List<SolicitudResponseDTO> listarSolicitudes();
    Map<String, Object> crearSolicitud(SolicitudRequestDTO req);
    void cambiarEstado(int id, String nuevoEstado);
    void eliminarSolicitud(int id);
}