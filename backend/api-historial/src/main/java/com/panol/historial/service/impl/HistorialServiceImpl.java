package com.panol.historial.service.impl;

import com.panol.historial.dto.HistorialCreateRequestDTO;
import com.panol.historial.dto.HistorialRow;
import com.panol.historial.entity.Historial;
import com.panol.historial.entity.Producto;
import com.panol.historial.repository.HistorialRepository;
import com.panol.historial.repository.ProductoRepository;
import com.panol.historial.service.HistorialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialServiceImpl implements HistorialService {

    private final HistorialRepository repo;
    private final ProductoRepository productoRepo;

    public HistorialServiceImpl(HistorialRepository repo,
            ProductoRepository productoRepo) {
        this.repo = repo;
        this.productoRepo = productoRepo;
    }

    @Override
    @Transactional
    public HistorialRow crearHistorial(HistorialCreateRequestDTO dto) {

        Producto producto = productoRepo.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no existe con id: " + dto.getProductoId()));

        Historial h = new Historial();
        h.setProducto(producto);
        h.setCantidad(dto.getCantidad());
        h.setFechaInicioPrestamo(dto.getFechaInicioPrestamo());
        h.setFechaRetornoPrestamo(dto.getFechaRetornoPrestamo());
        h.setFechaDevolucionPrestamo(dto.getFechaDevolucionPrestamo());

        Historial guardado = repo.save(h);
        return mapToDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialRow> listarHistorial() {
        return repo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialRow buscarPorId(int idDetalle) {
        Historial h = repo.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException(
                        "Registro de historial no encontrado con idDetalle: " + idDetalle));
        return mapToDTO(h);
    }

    @Override
    @Transactional
    public HistorialRow editarHistorial(int idDetalle, HistorialRow dto) {
        Historial h = repo.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException(
                        "Registro de historial no encontrado para editar con idDetalle: " + idDetalle));

        // Actualiza solo los campos presentes en el DTO
        if (dto.getCantidad() != null) {
            h.setCantidad(dto.getCantidad());
        }
        if (dto.getFechaInicioPrestamo() != null) {
            h.setFechaInicioPrestamo(dto.getFechaInicioPrestamo());
        }
        if (dto.getFechaRetornoPrestamo() != null) {
            h.setFechaRetornoPrestamo(dto.getFechaRetornoPrestamo());
        }
        if (dto.getFechaDevolucionPrestamo() != null) {
            h.setFechaDevolucionPrestamo(dto.getFechaDevolucionPrestamo());
        }

        Historial actualizado = repo.save(h);
        return mapToDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarHistorial(int idDetalle) {
        if (!repo.existsById(idDetalle)) {
            throw new RuntimeException("Registro de historial no encontrado para eliminar con idDetalle: " + idDetalle);
        }
        repo.deleteById(idDetalle);
    }

    /** Convierte una entidad Historial en su DTO correspondiente. */
    private HistorialRow mapToDTO(Historial h) {
        return HistorialRow.builder()
                .idDetalle(h.getIdDetalle())
                .nombreProducto(h.getProducto() != null ? h.getProducto().getNombre() : "Producto Desconocido")
                .cantidad(h.getCantidad())
                .fechaInicioPrestamo(h.getFechaInicioPrestamo())
                .fechaRetornoPrestamo(h.getFechaRetornoPrestamo())
                .fechaDevolucionPrestamo(h.getFechaDevolucionPrestamo())
                .build();
    }
}
