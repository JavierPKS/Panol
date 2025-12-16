package com.panol.inventario.service.impl;

import com.panol.inventario.dto.*;
import com.panol.inventario.exceptions.*;
import com.panol.inventario.models.*;
import com.panol.inventario.repository.*;
import com.panol.inventario.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

  public InventarioServiceImpl(ProductoRepository productoRepo, CategoriaProdRepository categoriaRepo,
      MarcaRepository marcaRepo, UbicacionInvRepository ubicacionRepo,
      StockRepository stockRepo, InventarioRepository inventarioRepo) {
    this.productoRepo = productoRepo;
    this.categoriaRepo = categoriaRepo;
    this.marcaRepo = marcaRepo;
    this.ubicacionRepo = ubicacionRepo;
    this.stockRepo = stockRepo;
    this.inventarioRepo = inventarioRepo;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductoResponseDTO> listarInventario() {
    return productoRepo.findAll().stream()
        .filter(p -> "1".equals(p.getEstado())) // Solo listar activos
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, Object> obtenerProducto(int id) {
    Producto p = productoRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

    Map<String, Object> res = new HashMap<>();
    res.put("id", p.getId());
    res.put("cod_interno", p.getCodInterno());
    res.put("nombre_producto", p.getNombreProducto());
    res.put("estado", p.getEstado());

    if (p.getCategoria() != null)
      res.put("categoria", p.getCategoria().getId());
    if (p.getMarca() != null)
      res.put("marca", p.getMarca().getId());

    if (p.getInventario() != null) {
      if (p.getInventario().getUbicacion() != null)
        res.put("ubicacion", p.getInventario().getUbicacion().getId());
      if (p.getInventario().getStock() != null)
        res.put("stock", p.getInventario().getStock().getCantidad());
    }
    return res;
  }

  @Override
  @Transactional
  public void crearProducto(ProductoRequestDTO req) {
    // 1. Validar que no exista el código interno
    if (productoRepo.existsByCodInterno(req.getCod_interno())) {
      throw new BadRequestException("El código interno ya existe en el sistema.");
    }

    // 2. Buscar entidades relacionadas
    CategoriaProd cat = categoriaRepo.findById(req.getCategoria())
        .orElseThrow(() -> new NotFoundException("Categoría no válida"));
    Marca mar = marcaRepo.findById(req.getMarca())
        .orElseThrow(() -> new NotFoundException("Marca no válida"));
    UbicacionInv ubi = ubicacionRepo.findById(req.getUbicacion())
        .orElseThrow(() -> new NotFoundException("Ubicación no válida"));

    // 3. Crear Stock
    Stock stock = Stock.builder()
        .cantidad(req.getCantidad())
        .stockMinimo(5) // Valor por defecto
        .stockMaximo(100)
        .build();

    // 4. Crear Inventario (vinculado a stock y ubicación)
    Inventario inv = Inventario.builder()
        .observacion("Ingreso Inicial")
        .fechaActualizacion(LocalDate.now())
        .ubicacion(ubi)
        .stock(stock)
        .build();

    // 5. Crear Producto
    Producto p = Producto.builder()
        .codInterno(req.getCod_interno())
        .nombreProducto(req.getNombre_producto())
        .estado("1") // Activo por defecto
        .categoria(cat)
        .marca(mar)
        .inventario(inv) // JPA guardará Inventario y Stock en cascada
        .build();

    productoRepo.save(p);
  }

  @Override
  @Transactional
  public void crearProducto(ProductoRequestDTO req, MultipartFile file) {
    // Por compatibilidad: delega a la versión sin archivo.
    crearProducto(req);
  }

  @Override
  @Transactional
  public void editarProducto(int id, ProductoEditRequestDTO req) {
    Producto p = productoRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

    CategoriaProd cat = categoriaRepo.findById(req.getCategoria())
        .orElseThrow(() -> new NotFoundException("Categoría no válida"));
    Marca mar = marcaRepo.findById(req.getMarca())
        .orElseThrow(() -> new NotFoundException("Marca no válida"));
    UbicacionInv ubi = ubicacionRepo.findById(req.getUbicacion())
        .orElseThrow(() -> new NotFoundException("Ubicación no válida"));

    // Actualizar datos básicos
    p.setNombreProducto(req.getNombre_producto());
    p.setCategoria(cat);
    p.setMarca(mar);
    if (req.getEstado() != null)
      p.setEstado(req.getEstado());

    // Actualizar Inventario y Ubicación
    Inventario inv = p.getInventario();
    if (inv == null) {
      // Caso borde: si no existiera inventario (no debería pasar)
      inv = new Inventario();
      inv.setFechaActualizacion(LocalDate.now());
      p.setInventario(inv);
    }
    inv.setUbicacion(ubi);
    inv.setFechaActualizacion(LocalDate.now());

    // Actualizar Stock
    Stock stock = inv.getStock();
    if (stock == null) {
      stock = new Stock();
      inv.setStock(stock);
    }
    stock.setCantidad(req.getStock());

    productoRepo.save(p); // Cascada guarda todo
  }

  @Override
  @Transactional
  public void editarProducto(int id, ProductoEditRequestDTO req, MultipartFile file) {
    // Por compatibilidad: delega a la versión sin archivo.
    editarProducto(id, req);
  }

  @Override
  @Transactional
  public void eliminarProducto(int id) {
    Producto p = productoRepo.findById(id)
        .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
    p.setEstado("0"); // Eliminación lógica
    productoRepo.save(p);
  }

  // --- Métodos Auxiliares ---

  private ProductoResponseDTO mapToDTO(Producto p) {
    int cantidad = (p.getInventario() != null && p.getInventario().getStock() != null)
        ? p.getInventario().getStock().getCantidad()
        : 0;

    return ProductoResponseDTO.builder()
        .id(p.getId())
        .codigo(p.getCodInterno())
        .nombre(p.getNombreProducto())
        .categoria(p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin Categ.")
        .stock_total(cantidad)
        .stock_disponible(cantidad) // Por ahora igual, luego se descuenta prestados si es necesario
        .stock_prestado(0)
        .estado(p.getEstado())
        .build();
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
  public List<Map<String, Object>> listarUbicaciones() {
    return ubicacionRepo.findAll().stream().map(u -> {
      Map<String, Object> m = new HashMap<>();
      m.put("id_ubicacion", u.getId());
      m.put("nombre", u.getNombreSala() + " - " + u.getEstante());
      return m;
    }).collect(Collectors.toList());
  }

  @Override
  public Map<String, Object> crearCategoria(String nombre) {
    CategoriaProd c = new CategoriaProd();
    c.setNombre(nombre);
    c = categoriaRepo.save(c);
    return Map.of("id", c.getId(), "nombre", c.getNombre());
  }

  @Override
  public Map<String, Object> crearMarca(String nombre) {
    Marca m = new Marca();
    m.setNombre(nombre);
    m = marcaRepo.save(m);
    return Map.of("id", m.getId(), "nombre", m.getNombre());
  }

  @Override
  public Map<String, Object> crearUbicacion(String sala, String estante, Integer nivel, String descripcion) {
    UbicacionInv u = UbicacionInv.builder()
        .nombreSala(sala).estante(estante).nivel(nivel).descripcion(descripcion).build();
    u = ubicacionRepo.save(u);
    return Map.of("id", u.getId(), "nombre", sala + " - " + estante);
  }
}