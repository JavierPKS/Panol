package com.panol.inventario.service;

import com.panol.inventario.dto.*;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio de dominio para la gestión del inventario.
 * 
 * Define las operaciones de consulta y mantenimiento tanto para productos
 * como para las entidades auxiliares (categorías, marcas y ubicaciones).
 */
public interface InventarioService {

    /**
     * Obtiene el listado completo de productos activos en el inventario.
     *
     * @return una lista de productos con información de stock y estado
     */
    List<ProductoResponseDTO> listarInventario();

    /**
     * Obtiene un producto en particular junto con sus atributos básicos.
     *
     * @param id identificador del producto
     * @return un mapa con los campos básicos del producto
     */
    Map<String, Object> obtenerProducto(int id);

    /**
     * Crea un nuevo producto en el inventario.
     *
     * @param req datos del producto a crear
     */
    void crearProducto(ProductoRequestDTO req);

    /**
     * Crea un nuevo producto con imagen asociada.
     *
     * Este método se expone para permitir futuras extensiones en las que se
     * gestione la subida y almacenamiento de imágenes. Actualmente delega
     * internamente en {@link #crearProducto(ProductoRequestDTO)}.
     *
     * @param req datos del producto
     * @param file archivo de imagen
     */
    void crearProducto(ProductoRequestDTO req, MultipartFile file);

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id  identificador del producto
     * @param req nuevo estado del producto
     */
    void editarProducto(int id, ProductoEditRequestDTO req);

    /**
     * Actualiza los datos de un producto existente incluyendo su imagen.
     *
     * @param id  identificador del producto
     * @param req nuevo estado del producto
     * @param file imagen a asociar
     */
    void editarProducto(int id, ProductoEditRequestDTO req, MultipartFile file);

    /**
     * Elimina lógicamente un producto del inventario. Se marca como inactivo.
     *
     * @param id identificador del producto
     */
    void eliminarProducto(int id);

    // ----- Operaciones sobre categorías -----

    /**
     * Devuelve el listado de todas las categorías.
     *
     * @return lista de categorías
     */
    List<CategoriaResponseDTO> listarCategorias();

    /**
     * Obtiene una categoría por su identificador.
     *
     * @param id identificador de la categoría
     * @return DTO con los datos de la categoría
     */
    CategoriaResponseDTO obtenerCategoria(int id);

    /**
     * Crea una nueva categoría.
     *
     * @param dto datos de la categoría
     * @return categoría creada
     */
    CategoriaResponseDTO crearCategoria(CategoriaRequestDTO dto);

    /**
     * Actualiza una categoría existente.
     *
     * @param id  identificador de la categoría
     * @param dto datos a actualizar
     * @return categoría actualizada
     */
    CategoriaResponseDTO actualizarCategoria(int id, CategoriaRequestDTO dto);

    /**
     * Elimina una categoría.
     *
     * @param id identificador de la categoría
     */
    void eliminarCategoria(int id);

    // ----- Operaciones sobre marcas -----

    /**
     * Devuelve el listado de todas las marcas.
     *
     * @return lista de marcas
     */
    List<MarcaResponseDTO> listarMarcas();

    /**
     * Obtiene una marca por su identificador.
     *
     * @param id identificador de la marca
     * @return DTO con los datos de la marca
     */
    MarcaResponseDTO obtenerMarca(int id);

    /**
     * Crea una nueva marca.
     *
     * @param dto datos de la marca
     * @return marca creada
     */
    MarcaResponseDTO crearMarca(MarcaRequestDTO dto);

    /**
     * Actualiza una marca existente.
     *
     * @param id  identificador de la marca
     * @param dto datos a actualizar
     * @return marca actualizada
     */
    MarcaResponseDTO actualizarMarca(int id, MarcaRequestDTO dto);

    /**
     * Elimina una marca.
     *
     * @param id identificador de la marca
     */
    void eliminarMarca(int id);

    // ----- Operaciones sobre ubicaciones -----

    /**
     * Devuelve el listado de todas las ubicaciones del inventario.
     *
     * @return lista de ubicaciones
     */
    List<UbicacionResponseDTO> listarUbicaciones();

    /**
     * Obtiene una ubicación por su identificador.
     *
     * @param id identificador de la ubicación
     * @return DTO con los datos de la ubicación
     */
    UbicacionResponseDTO obtenerUbicacion(int id);

    /**
     * Crea una nueva ubicación.
     *
     * @param dto datos de la ubicación
     * @return ubicación creada
     */
    UbicacionResponseDTO crearUbicacion(UbicacionRequestDTO dto);

    /**
     * Actualiza una ubicación existente.
     *
     * @param id  identificador de la ubicación
     * @param dto datos a actualizar
     * @return ubicación actualizada
     */
    UbicacionResponseDTO actualizarUbicacion(int id, UbicacionRequestDTO dto);

    /**
     * Elimina una ubicación.
     *
     * @param id identificador de la ubicación
     */
    void eliminarUbicacion(int id);
}