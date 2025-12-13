package com.panol.inventario.service;

import com.panol.inventario.dto.*;
import com.panol.inventario.entity.*;
import com.panol.inventario.repository.*;
import com.panol.prestamos.repository.DetPrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class InventarioService {

  private final ProductoRepository productoRepo;
  private final CategoriaProdRepository categoriaRepo;
  private final MarcaRepository marcaRepo;
  private final UbicacionInvRepository ubicacionRepo;
  private final StockRepository stockRepo;
  private final InventarioRepository inventarioRepo;
  private final DetPrestamoRepository detPrestamoRepo;

  public InventarioService(
      ProductoRepository productoRepo,
      CategoriaProdRepository categoriaRepo,
      MarcaRepository marcaRepo,
      UbicacionInvRepository ubicacionRepo,
      StockRepository stockRepo,
      InventarioRepository inventarioRepo,
      DetPrestamoRepository detPrestamoRepo
  ) {
    this.productoRepo = productoRepo;
    this.categoriaRepo = categoriaRepo;
    this.marcaRepo = marcaRepo;
    this.ubicacionRepo = ubicacionRepo;
    this.stockRepo = stockRepo;
    this.inventarioRepo = inventarioRepo;
    this.detPrestamoRepo = detPrestamoRepo;
  }

  public List<ProductoResponse> listarInventario() {
    List<Producto> productos = productoRepo.findAll();
    List<ProductoResponse> out = new ArrayList<>();

    for (Producto p : productos) {
      int disponible = p.getInventario().getStock().getCantidad();
      int prestado = detPrestamoRepo.sumCantidadByProductoId(p.getId());
      int total = disponible + prestado;

      ProductoResponse r = new ProductoResponse();
      r.id = p.getId();
      r.nombre = p.getNombreProducto();
      r.codigo = p.getCodInterno();
      r.categoria = p.getCategoria().getNombre();
      r.stock_disponible = disponible;
      r.stock_prestado = prestado;
      r.stock_total = total;
      r.estado = p.getEstado();
      out.add(r);
    }
    return out;
  }

  public Map<String, Object> obtenerProducto(int id) {
    Producto p = productoRepo.findById(id).orElseThrow();
    Map<String, Object> res = new HashMap<>();
    res.put("id", p.getId());
    res.put("cod_interno", p.getCodInterno());
    res.put("nombre_producto", p.getNombreProducto());
    res.put("estado", p.getEstado());
    res.put("categoria", p.getCategoria().getId());
    res.put("marca", p.getMarca().getId());
    res.put("ubicacion", p.getInventario().getUbicacion().getId());
    res.put("stock", p.getInventario().getStock().getCantidad());
    return res;
  }

  @Transactional
  public void crearProducto(ProductoRequest req) {
    CategoriaProd cat = categoriaRepo.findById(req.categoria).orElseThrow();
    Marca mar = marcaRepo.findById(req.marca).orElseThrow();
    UbicacionInv ubi = ubicacionRepo.findById(req.ubicacion).orElseThrow();

    Stock stock = new Stock();
    stock.setStockMinimo(0);
    stock.setStockMaximo(0);
    stock.setCantidad(req.cantidad);
    stock = stockRepo.save(stock);

    Inventario inv = new Inventario();
    inv.setObservacion("Ingreso inicial");
    inv.setFechaActualizacion(LocalDate.now());
    inv.setUbicacion(ubi);
    inv.setStock(stock);
    inv = inventarioRepo.save(inv);

    Producto p = new Producto();
    p.setCodInterno(req.cod_interno);
    p.setNombreProducto(req.nombre_producto);
    p.setEstado("1"); // según tu CHECK en PRODUCTO :contentReference[oaicite:4]{index=4}
    p.setCategoria(cat);
    p.setMarca(mar);
    p.setInventario(inv);

    productoRepo.save(p);
  }

  @Transactional
  public void editarProducto(int id, ProductoEditRequest req) {
    Producto p = productoRepo.findById(id).orElseThrow();

    CategoriaProd cat = categoriaRepo.findById(req.categoria).orElseThrow();
    Marca mar = marcaRepo.findById(req.marca).orElseThrow();
    UbicacionInv ubi = ubicacionRepo.findById(req.ubicacion).orElseThrow();

    p.setNombreProducto(req.nombre_producto);
    p.setEstado(req.estado);
    p.setCategoria(cat);
    p.setMarca(mar);

    Inventario inv = p.getInventario();
    inv.setUbicacion(ubi);
    inv.setFechaActualizacion(LocalDate.now());

    Stock stock = inv.getStock();
    stock.setCantidad(req.stock);

    // save (por cascada no estamos usando, así que guardamos explícito)
    stockRepo.save(stock);
    inventarioRepo.save(inv);
    productoRepo.save(p);
  }

  @Transactional
  public void eliminarProducto(int id) {
    Producto p = productoRepo.findById(id).orElseThrow();
    // Ojo: en tu BD hay FK desde DET_PRESTAMO a PRODUCTO :contentReference[oaicite:5]{index=5}
    // si existen préstamos asociados, MySQL impedirá borrar.
    productoRepo.delete(p);
  }

  public List<CategoriaProd> listarCategorias() { return categoriaRepo.findAll(); }
  public List<Marca> listarMarcas() { return marcaRepo.findAll(); }
  public List<Map<String,Object>> listarUbicaciones() {
    return ubicacionRepo.findAll().stream().map(u -> {
      Map<String,Object> m = new HashMap<>();
      m.put("id_ubicacion", u.getId());
      m.put("nombre", u.getNombreSala()+" - Estante "+u.getEstante()+" Nivel "+u.getNivel());
      return m;
    }).toList();
  }

  @Transactional
  public Map<String,Object> crearCategoria(String nombre) {
    CategoriaProd c = new CategoriaProd();
    c.setNombre(nombre);
    c = categoriaRepo.save(c);
    return Map.of("id", c.getId(), "nombre", c.getNombre());
  }

  @Transactional
  public Map<String,Object> crearMarca(String nombre) {
    Marca m = new Marca();
    m.setNombre(nombre);
    m = marcaRepo.save(m);
    return Map.of("id", m.getId(), "nombre", m.getNombre());
  }

  @Transactional
  public Map<String,Object> crearUbicacion(String sala, String estante, Integer nivel, String descripcion) {
    UbicacionInv u = new UbicacionInv();
    u.setNombreSala(sala);
    u.setEstante(estante);
    u.setNivel(nivel);
    u.setDescripcion(descripcion);
    u = ubicacionRepo.save(u);
    String nombre = sala + " - Estante " + estante + " Nivel " + nivel;
    return Map.of("id", u.getId(), "nombre", nombre);
  }
}
