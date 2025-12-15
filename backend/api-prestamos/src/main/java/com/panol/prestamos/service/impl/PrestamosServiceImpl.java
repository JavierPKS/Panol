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
    public List<PrestamoResponseDTO> listarPrestamos() {
        // JPA busca todos y nosotros los convertimos a DTO
        return detRepo.findAll().stream().map(det -> 
            PrestamoResponseDTO.builder()
                .id_detalle(det.getIdDetalle())
                .nombre_producto(det.getProducto().getNombre()) // Obtenemos nombre SIN QUERY NATIVA
                .cantidad(det.getCantidad())
                .fecha_incio_prestamo(det.getFechaInicioPrestamo())
                .fecha_devolucion_prestamo(det.getFechaDevolucionPrestamo())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void prestar(PrestarRequestDTO req) {
        Producto producto = productoRepo.findById(req.getId_producto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Acceso JPA a Stock: Producto -> Inventario -> Stock
        if(producto.getInventario() == null || producto.getInventario().getStock() == null) {
             throw new RuntimeException("El producto no tiene inventario asociado");
        }
        
        Stock stock = producto.getInventario().getStock();

        if (stock.getCantidad() < req.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Crear detalle de préstamo
        DetPrestamo det = new DetPrestamo();
        det.setCantidad(req.getCantidad());
        det.setFechaInicioPrestamo(LocalDate.now());
        det.setFechaRetornoPrestamo(LocalDate.now().plusDays(7));
        det.setIdPrestamo(req.getId_prestamo());
        det.setProducto(producto); // Asignamos la entidad completa

        detRepo.save(det);

        // Descontar stock
        stock.setCantidad(stock.getCantidad() - req.getCantidad());
        stockRepo.save(stock);
    }

    @Override
    @Transactional
    public void devolver(DevolverRequestDTO req) {
        DetPrestamo det = detRepo.findById(req.getId_detalle())
                .orElseThrow(() -> new RuntimeException("Detalle de préstamo no encontrado"));

        if(det.getFechaDevolucionPrestamo() != null) {
            throw new RuntimeException("Este préstamo ya fue devuelto");
        }

        det.setFechaDevolucionPrestamo(LocalDate.now());
        detRepo.save(det);

        // Reponer stock
        Stock stock = det.getProducto().getInventario().getStock();
        stock.setCantidad(stock.getCantidad() + req.getCantidad());
        stockRepo.save(stock);
    }
}