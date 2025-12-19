package com.panol.inventario.service.impl;

import com.panol.inventario.dto.*;
import com.panol.inventario.exceptions.BadRequestException;
import com.panol.inventario.exceptions.NotFoundException;
import com.panol.inventario.models.*;
import com.panol.inventario.repository.*;
import com.panol.inventario.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación de {@link InventarioService} con correcciones para
 * integridad referencial de la base de datos.
 */
@Service
public class InventarioServiceImpl implements InventarioService {

    private final ProductoRepository productoRepo;
    private final CategoriaProdRepository categoriaRepo;
    private final MarcaRepository marcaRepo;
    private final UbicacionInvRepository ubicacionRepo;
    private final StockRepository stockRepo;
    private final InventarioRepository inventarioRepo;

    public InventarioServiceImpl(ProductoRepository productoRepo,
                                 CategoriaProdRepository categoriaRepo,
                                 MarcaRepository marcaRepo,
                                 UbicacionInvRepository ubicacionRepo,
                                 StockRepository stockRepo,
                                 InventarioRepository inventarioRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
        this.marcaRepo = marcaRepo;
        this.ubicacionRepo = ubicacionRepo;
        this.stockRepo = stockRepo;
        this.inventarioRepo = inventarioRepo;
    }

    // ----- Productos -----

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
            res.put("categoria_id", p.getCategoria().getId()); // Ajustado nombre clave
        if (p.getMarca() != null)
            res.put("marca_id", p.getMarca().getId()); // Ajustado nombre clave

        if (p.getInventario() != null) {
            if (p.getInventario().getUbicacion() != null)
                res.put("ubicacion_id", p.getInventario().getUbicacion().getId()); // Ajustado
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
                .stockMinimo(5)
                .stockMaximo(100)
                .build();

        // 4. Crear Inventario
        Inventario inv = Inventario.builder()
                .observacion("Ingreso Inicial")
                .fechaActualizacion(LocalDate.now())
                .ubicacion(ubi)
                .stock(stock)
                .build();

        // 5. Crear Producto
        // CORRECCIÓN: Asignamos estadoId y disponibilidadId requeridos por BD
        Producto p = Producto.builder()
                .codInterno(req.getCod_interno())
                .nombreProducto(req.getNombre_producto())
                .estado("1") // String estado
                .estadoId(1) // ID tabla ESTADO (1=Activo)
                .disponibilidadId(1) // ID tabla DISPONIBILIDAD (1=Disponible)
                .categoria(cat)
                .marca(mar)
                .inventario(inv)
                .build();

        productoRepo.save(p);
    }

    @Override
    @Transactional
    public void crearProducto(ProductoRequestDTO req, MultipartFile file) {
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
        if (req.getEstado() != null) {
            p.setEstado(req.getEstado());
            // Si el estado cambia a "0" (Eliminado), podríamos actualizar estadoId a 2 o 3 según BD
            // Por simplicidad mantenemos 1, ya que la lógica principal usa el String "estado"
        }

        // Actualizar Inventario
        Inventario inv = p.getInventario();
        if (inv == null) {
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

        productoRepo.save(p);
    }

    @Override
    @Transactional
    public void editarProducto(int id, ProductoEditRequestDTO req, MultipartFile file) {
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

    // ----- Categorías, Marcas, Ubicaciones (Sin cambios mayores) -----

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepo.findAll().stream().map(this::mapCategoriaToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerCategoria(int id) {
        CategoriaProd c = categoriaRepo.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        return mapCategoriaToDTO(c);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO dto) {
        CategoriaProd c = new CategoriaProd();
        c.setNombre(dto.getNombre());
        c = categoriaRepo.save(c);
        return mapCategoriaToDTO(c);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO actualizarCategoria(int id, CategoriaRequestDTO dto) {
        CategoriaProd c = categoriaRepo.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        c.setNombre(dto.getNombre());
        c = categoriaRepo.save(c);
        return mapCategoriaToDTO(c);
    }

    @Override
    @Transactional
    public void eliminarCategoria(int id) {
        CategoriaProd c = categoriaRepo.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        categoriaRepo.delete(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> listarMarcas() {
        return marcaRepo.findAll().stream().map(this::mapMarcaToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaResponseDTO obtenerMarca(int id) {
        Marca m = marcaRepo.findById(id).orElseThrow(() -> new NotFoundException("Marca no encontrada"));
        return mapMarcaToDTO(m);
    }

    @Override
    @Transactional
    public MarcaResponseDTO crearMarca(MarcaRequestDTO dto) {
        Marca m = new Marca();
        m.setNombre(dto.getNombre());
        m = marcaRepo.save(m);
        return mapMarcaToDTO(m);
    }

    @Override
    @Transactional
    public MarcaResponseDTO actualizarMarca(int id, MarcaRequestDTO dto) {
        Marca m = marcaRepo.findById(id).orElseThrow(() -> new NotFoundException("Marca no encontrada"));
        m.setNombre(dto.getNombre());
        m = marcaRepo.save(m);
        return mapMarcaToDTO(m);
    }

    @Override
    @Transactional
    public void eliminarMarca(int id) {
        Marca m = marcaRepo.findById(id).orElseThrow(() -> new NotFoundException("Marca no encontrada"));
        marcaRepo.delete(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbicacionResponseDTO> listarUbicaciones() {
        return ubicacionRepo.findAll().stream().map(this::mapUbicacionToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UbicacionResponseDTO obtenerUbicacion(int id) {
        UbicacionInv u = ubicacionRepo.findById(id).orElseThrow(() -> new NotFoundException("Ubicación no encontrada"));
        return mapUbicacionToDTO(u);
    }

    @Override
    @Transactional
    public UbicacionResponseDTO crearUbicacion(UbicacionRequestDTO dto) {
        UbicacionInv u = UbicacionInv.builder()
                .nombreSala(dto.getNombreSala())
                .estante(dto.getEstante())
                .nivel(dto.getNivel())
                .descripcion(dto.getDescripcion())
                .build();
        u = ubicacionRepo.save(u);
        return mapUbicacionToDTO(u);
    }

    @Override
    @Transactional
    public UbicacionResponseDTO actualizarUbicacion(int id, UbicacionRequestDTO dto) {
        UbicacionInv u = ubicacionRepo.findById(id).orElseThrow(() -> new NotFoundException("Ubicación no encontrada"));
        u.setNombreSala(dto.getNombreSala());
        u.setEstante(dto.getEstante());
        u.setNivel(dto.getNivel());
        u.setDescripcion(dto.getDescripcion());
        u = ubicacionRepo.save(u);
        return mapUbicacionToDTO(u);
    }

    @Override
    @Transactional
    public void eliminarUbicacion(int id) {
        UbicacionInv u = ubicacionRepo.findById(id).orElseThrow(() -> new NotFoundException("Ubicación no encontrada"));
        ubicacionRepo.delete(u);
    }

    // ----- Mappers -----

    private ProductoResponseDTO mapToDTO(Producto p) {
        int cantidad = (p.getInventario() != null && p.getInventario().getStock() != null)
                ? p.getInventario().getStock().getCantidad() : 0;

        String nombreUbicacion = "Sin Ubicación";
        if (p.getInventario() != null && p.getInventario().getUbicacion() != null) {
            nombreUbicacion = p.getInventario().getUbicacion().getNombreSala()
                    + " - " + p.getInventario().getUbicacion().getEstante();
        }

        return ProductoResponseDTO.builder()
                .id(p.getId())
                .codigo(p.getCodInterno())
                .nombre(p.getNombreProducto())
                .categoria(p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin Categ.")
                .marca(p.getMarca() != null ? p.getMarca().getNombre() : "Sin Marca")
                .ubicacion(nombreUbicacion)
                .stock_total(cantidad)
                .stock_disponible(cantidad) // Lógica simple inicial
                .stock_prestado(0)
                .estado(p.getEstado())
                .build();
    }

    private CategoriaResponseDTO mapCategoriaToDTO(CategoriaProd c) {
        return CategoriaResponseDTO.builder().id(c.getId()).nombre(c.getNombre()).build();
    }
    private MarcaResponseDTO mapMarcaToDTO(Marca m) {
        return MarcaResponseDTO.builder().id(m.getId()).nombre(m.getNombre()).build();
    }
    private UbicacionResponseDTO mapUbicacionToDTO(UbicacionInv u) {
        return UbicacionResponseDTO.builder()
                .id(u.getId())
                .nombreSala(u.getNombreSala())
                .estante(u.getEstante())
                .nivel(u.getNivel())
                .descripcion(u.getDescripcion())
                .build();
    }
}