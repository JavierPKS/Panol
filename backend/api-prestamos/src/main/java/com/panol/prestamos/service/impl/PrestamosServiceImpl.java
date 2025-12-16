package com.panol.prestamos.service.impl;

import com.panol.prestamos.dto.*;
import com.panol.prestamos.entity.*;
import com.panol.prestamos.repository.*;
import com.panol.prestamos.service.PrestamosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamosServiceImpl implements PrestamosService {

    private final DetPrestamoRepository detRepo;
    private final ProductoRepository productoRepo;
    private final StockRepository stockRepo;

    public PrestamosServiceImpl(DetPrestamoRepository detRepo, ProductoRepository productoRepo, StockRepository stockRepo) {
        this.detRepo = detRepo;
        this.productoRepo = productoRepo;
        this.stockRepo = stockRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarPrestamos() {
        return detRepo.findAll().stream().map(det -> 
            PrestamoResponseDTO.builder()
                .id_detalle(det.getIdDetalle())
                .nombre_producto(det.getProducto() != null ? det.getProducto().getNombre() : "Sin Nombre")
                .cantidad(det.getCantidad())
                .fecha_incio_prestamo(det.getFechaInicioPrestamo())
                .fecha_devolucion_prestamo(det.getFechaDevolucionPrestamo())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void prestar(PrestarRequestDTO req) {
        // 1. Obtener producto
        Producto producto = productoRepo.findById(req.getId_producto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // 2. Validar integridad del Stock
        if (producto.getInventario() == null || producto.getInventario().getStock() == null) {
             throw new RuntimeException("El producto no tiene stock configurado");
        }
        
        Stock stock = producto.getInventario().getStock();

        // 3. Validar cantidad disponible
        if (stock.getCantidad() < req.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        // 4. Registrar el préstamo
        DetPrestamo det = new DetPrestamo();
        det.setCantidad(req.getCantidad());
        det.setFechaInicioPrestamo(LocalDate.now());
        det.setFechaRetornoPrestamo(LocalDate.now().plusDays(7)); // Lógica: 7 días para devolver
        det.setIdPrestamo(req.getId_prestamo());
        det.setProducto(producto);

        detRepo.save(det);

        // 5. Descontar stock
        stock.setCantidad(stock.getCantidad() - req.getCantidad());
        stockRepo.save(stock);
    }

    @Override
    @Transactional
    public void devolver(DevolverRequestDTO req) {
        // 1. Buscar el detalle del préstamo
        DetPrestamo det = detRepo.findById(req.getId_detalle())
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // 2. Validar si ya fue devuelto
        if (det.getFechaDevolucionPrestamo() != null) {
            throw new RuntimeException("El préstamo ya fue devuelto anteriormente");
        }

        // 3. Registrar fecha de devolución real
        det.setFechaDevolucionPrestamo(LocalDate.now());
        detRepo.save(det);

        // 4. Reponer stock
        if (det.getProducto() != null && det.getProducto().getInventario() != null) {
            Stock stock = det.getProducto().getInventario().getStock();
            // Se repone la cantidad original del préstamo
            stock.setCantidad(stock.getCantidad() + det.getCantidad());
            stockRepo.save(stock);
        }
    }
}