package com.panol.historial.service.impl;

import com.panol.historial.dto.HistorialRow;
import com.panol.historial.entity.Historial;
import com.panol.historial.repository.HistorialRepository;
import com.panol.historial.service.HistorialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialServiceImpl implements HistorialService {

    private final HistorialRepository repo;

    public HistorialServiceImpl(HistorialRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialRow> obtenerHistorialGeneral() {
        // 1. Obtiene todas las entidades (hace el Join internamente)
        List<Historial> historialList = repo.findAll();

        // 2. Transforma (Mapea) la Entidad al DTO
        return historialList.stream().map(h -> HistorialRow.builder()
                .nombre_producto(h.getProducto() != null ? h.getProducto().getNombre() : "Producto Desconocido")
                .cantidad(h.getCantidad())
                .fecha_incio_prestamo(h.getFechaInicioPrestamo())
                .fecha_devolucion_prestamo(h.getFechaDevolucionPrestamo())
                .build()
        ).collect(Collectors.toList());
    }
}