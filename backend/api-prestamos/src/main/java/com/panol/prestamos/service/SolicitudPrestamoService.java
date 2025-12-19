package com.panol.prestamos.service;

import com.panol.prestamos.entity.SolicitudPrestamo;
import java.util.List;
import java.util.Optional;

public interface SolicitudPrestamoService {
    SolicitudPrestamo crear(SolicitudPrestamo solicitud);
    List<SolicitudPrestamo> listar();
    Optional<SolicitudPrestamo> buscarPorId(Long id);
    void finalizarPrestamo(Long id);
}