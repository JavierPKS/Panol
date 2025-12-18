package com.panol.prestamos.service.impl;

import com.panol.prestamos.entity.DetallePrestamo;
import com.panol.prestamos.repository.DetallePrestamoRepository;
import com.panol.prestamos.service.DetallePrestamoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DetallePrestamoServiceImpl
        implements DetallePrestamoService {

    private final DetallePrestamoRepository repo;

    public DetallePrestamoServiceImpl(DetallePrestamoRepository repo) {
        this.repo = repo;
    }

    public List<DetallePrestamo> buscarPorSolicitud(Long solicitudId) {
        return repo.findBySolicitudPrestamoId(solicitudId);
    }

    public DetallePrestamo guardar(DetallePrestamo detalle) {
        return repo.save(detalle);
    }
    
    public void registrarDevolucion(Long solicitudId) {
        List<DetallePrestamo> detalles =
                repo.findBySolicitudPrestamoId(solicitudId);

        detalles.forEach(d -> {
            d.setFechaDevolucionPrestamo(LocalDate.now());
            repo.save(d);
        });
    }
}
