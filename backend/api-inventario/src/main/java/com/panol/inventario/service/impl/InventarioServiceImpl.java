package com.panol.inventario.service.impl;

import com.panol.inventario.dto.*;
import com.panol.inventario.models.*;
import com.panol.inventario.repository.*;
import com.panol.inventario.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventarioServiceImpl implements InventarioService {

  private final ProductoRepository productoRepo;
  private final CategoriaProdRepository categoriaRepo;
  private final MarcaRepository marcaRepo;
  private final UbicacionInvRepository ubicacionRepo;
  private final StockRepository stockRepo;
  private final InventarioRepository inventarioRepo;

  public InventarioServiceImpl(
      ProductoRepository productoRepo,
      CategoriaProdRepository categoriaRepo,
      MarcaRepository marcaRepo,
      UbicacionInvRepository ubicacionRepo,
      StockRepository stockRepo,
      InventarioRepository inventarioRepo
  ) {
    this.productoRepo = productoRepo;
    this.categoriaRepo = categoriaRepo;
    this.marcaRepo = marcaRepo;
    this.ubicacionRepo = ubicacionRepo;
    this.stockRepo = stockRepo;
    this.inventarioRepo = inventarioRepo;
  }

  @Override
  public List<ProductoResponseDTO> listarInventario() {
    List<Producto> productos = productoRepo.findAll();
    List<ProductoResponseDTO> out = new ArrayList<>();

    for (Producto p : productos) {
      // Manejo seguro de nulos
      int disponible = (p.getInventario() != null && p.getInventario().getStock() != null) 
          ? p.getInventario().getStock().getCantidad() : 0;
      int total = disponible; 

      ProductoResponseDTO r = new ProductoResponseDTO();
      r.setId(p.getId());
      r.setNombre(p.getNombreProducto());
      r.setCodigo(p.getCodInterno());
      if (p.getCategoria() != null) {
          r.setCategoria(p.getCategoria().getNombre());
      }
      r.setStock_disponible(disponible);
      r.setStock_prestado(0); 
      r.setStock_total(total);
      r.setEstado(p.getEstado());
      out.add(r);
    }
    return out;
  }

  @Override
  public Map<String, Object> obtenerProducto(int id) {
    Producto p = productoRepo.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    Map<String, Object> res = new HashMap<>();
    res.put("id", p.getId());
    res.put("cod_interno", p.getCodInterno());
    res.put("nombre_producto", p.getNombreProducto());
    res.put("estado", p.getEstado());
    if (p.getCategoria() != null) res.put("categoria", p.getCategoria().getId());
    if (p.getMarca() != null) res.put("marca", p.getMarca().getId());
    
    if (p.getInventario() != null) {
        if(p.getInventario().getUbicacion() != null) 
            res.put("ubicacion", p.getInventario().getUbicacion().getId());
        if(p.getInventario().getStock() != null)
            res.put("stock", p.getInventario().getStock().getCantidad());
    }
    return res;
  }

  @Override
  @Transactional
  public void crearProducto(ProductoRequestDTO req) {
    CategoriaProd cat = categoriaRepo.findById(req.getCategoria())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    Marca mar = marcaRepo.findById(req.getMarca())
        .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
    UbicacionInv ubi = ubicacionRepo.findById(req.getUbicacion())
        .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

    Stock stock = new Stock();
    stock.setStockMinimo(0);
    stock.setStockMaximo(0);
    stock.setCantidad(req.getCantidad());
    stock = stockRepo.save(stock);

    Inventario inv = new Inventario();
    inv.setObservacion("Ingreso inicial");
    inv.setFechaActualizacion(LocalDate.now());
    inv.setUbicacion(ubi);
    inv.setStock(stock);
    inv = inventarioRepo.save(inv);

    Producto p = new Producto();
    p.setCodInterno(req.getCod_interno());
    p.setNombreProducto(req.getNombre_producto());
    p.setEstado("1");
    p.setCategoria(cat);
    p.setMarca(mar);
    p.setInventario(inv);

    productoRepo.save(p);
  }

  @Override
  @Transactional
  public void editarProducto(int id, ProductoEditRequestDTO req) {
    Producto p = productoRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    CategoriaProd cat = categoriaRepo.findById(req.getCategoria())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    Marca mar = marcaRepo.findById(req.getMarca())
        .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
    UbicacionInv ubi = ubicacionRepo.findById(req.getUbicacion())
        .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

    p.setNombreProducto(req.getNombre_producto());
    p.setEstado(req.getEstado());
    p.setCategoria(cat);
    p.setMarca(mar);

    Inventario inv = p.getInventario();
    inv.setUbicacion(ubi);
    inv.setFechaActualizacion(LocalDate.now());

    Stock stock = inv.getStock();
    stock.setCantidad(req.getStock());

    stockRepo.save(stock);
    inventarioRepo.save(inv);
    productoRepo.save(p);
  }

  @Override
  @Transactional
  public void eliminarProducto(int id) {
        Producto p = productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setEstado("0");
        productoRepo.save(p);
    }

  @Override
  public List<CategoriaProd> listarCategorias() { 
      return categoriaRepo.findAll(); 
  }

  @Override
  public List<Marca> listarMarcas() { 
      return marcaRepo.findAll(); 
  }
  
  @Override
  public List<Map<String,Object>> listarUbicaciones() {
    return ubicacionRepo.findAll().stream().map(u -> {
      Map<String,Object> m = new HashMap<>();
      m.put("id_ubicacion", u.getId());
      m.put("nombre", u.getNombreSala() + " - Estante " + u.getEstante() + " Nivel " + u.getNivel());
      return m;
    }).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Map<String,Object> crearCategoria(String nombre) {
    CategoriaProd c = new CategoriaProd();
    c.setNombre(nombre);
    c = categoriaRepo.save(c);
    Map<String,Object> result = new HashMap<>();
    result.put("id", c.getId());
    result.put("nombre", c.getNombre());
    return result;
  }

  @Override
  @Transactional
  public Map<String,Object> crearMarca(String nombre) {
    Marca m = new Marca();
    m.setNombre(nombre);
    m = marcaRepo.save(m);
    Map<String,Object> result = new HashMap<>();
    result.put("id", m.getId());
    result.put("nombre", m.getNombre());
    return result;
  }

  @Override
  @Transactional
  public Map<String,Object> crearUbicacion(String sala, String estante, Integer nivel, String descripcion) {
    UbicacionInv u = new UbicacionInv();
    u.setNombreSala(sala);
    u.setEstante(estante);
    u.setNivel(nivel);
    u.setDescripcion(descripcion);
    u = ubicacionRepo.save(u);
    String nombre = sala + " - Estante " + estante + " Nivel " + nivel;
    Map<String,Object> result = new HashMap<>();
    result.put("id", u.getId());
    result.put("nombre", nombre);
    return result;
  }
}