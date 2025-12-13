package com.panol.prestamos.service;

import com.panol.inventario.entity.Producto;
import com.panol.inventario.entity.Stock;
import com.panol.inventario.repository.ProductoRepository;
import com.panol.inventario.repository.StockRepository;
import com.panol.prestamos.dto.*;
import com.panol.prestamos.entity.DetPrestamo;
import com.panol.prestamos.repository.DetPrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamosService {

  private final DetPrestamoRepository detRepo;
  private final ProductoRepository productoRepo;
  private final StockRepository stockRepo;

  public PrestamosService(DetPrestamoRepository detRepo, ProductoRepository productoRepo, StockRepository stockRepo) {
    this.detRepo = detRepo;
    this.productoRepo = productoRepo;
    this.stockRepo = stockRepo;
  }

  public List<PrestamoRow> listar() {
    return detRepo.listarPrestamos();
  }

  @Transactional
  public void prestar(PrestarRequest req) {
    Producto producto = productoRepo.findById(req.id_producto).orElseThrow();
    Stock stock = producto.getInventario().getStock();

    if (stock.getCantidad() < req.cantidad) {
      throw new RuntimeException("Stock insuficiente");
    }

    DetPrestamo det = new DetPrestamo();
    det.setCantidad(req.cantidad);
    det.setFechaInicioPrestamo(LocalDate.now());
    det.setFechaRetornoPrestamo(LocalDate.now().plusDays(7)); // regla simple
    det.setFechaDevolucionPrestamo(null);
    det.setIdPrestamo(req.id_prestamo);
    det.setIdProducto(req.id_producto);

    detRepo.save(det);

    stock.setCantidad(stock.getCantidad() - req.cantidad);
    stockRepo.save(stock);
  }

  @Transactional
  public void devolver(DevolverRequest req) {
    DetPrestamo det = detRepo.findById(req.id_detalle).orElseThrow();
    det.setFechaDevolucionPrestamo(LocalDate.now());
    detRepo.save(det);

    Producto producto = productoRepo.findById(req.id_producto).orElseThrow();
    Stock stock = producto.getInventario().getStock();
    stock.setCantidad(stock.getCantidad() + req.cantidad);
    stockRepo.save(stock);
  }
}
