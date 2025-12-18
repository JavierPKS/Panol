package com.panol.prestamos.service;

import com.panol.prestamos.entity.DetallePrestamo;
import java.util.List;

public interface DetallePrestamoService {
    List<DetallePrestamo> buscarPorSolicitud(Long solicitudId);
    DetallePrestamo guardar(DetallePrestamo detalle);
    void registrarDevolucion(Long solicitudId);
}
