package com.panol.prestamos.service.impl;

import com.panol.prestamos.entity.SolicitudPrestamo;
import com.panol.prestamos.repository.SolicitudPrestamoRepository;
import com.panol.prestamos.service.SolicitudPrestamoService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudPrestamoServiceImpl
        implements SolicitudPrestamoService {

    private final SolicitudPrestamoRepository repo;

    public SolicitudPrestamoServiceImpl(SolicitudPrestamoRepository repo) {
        this.repo = repo;
    }

    public SolicitudPrestamo crear(SolicitudPrestamo solicitud) {
        return repo.save(solicitud);
    }

    public List<SolicitudPrestamo> listar() {
        return repo.findAll();
    }

    public Optional<SolicitudPrestamo> buscarPorId(Long id) {
        return repo.findById(id);
    }
}
