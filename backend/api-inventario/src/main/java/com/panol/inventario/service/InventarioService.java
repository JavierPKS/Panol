package com.panol.inventario.service;

import com.panol.inventario.dto.*;
import com.panol.inventario.models.*;
import org.springframework.web.multipart.MultipartFile; // Import necesario para imágenes

import java.util.List;
import java.util.Map;

public interface InventarioService {

    List<ProductoResponseDTO> listarInventario();

    Map<String, Object> obtenerProducto(int id);

    void crearProducto(ProductoRequestDTO req);

    // Método para crear producto con imagen
    void crearProducto(ProductoRequestDTO req, MultipartFile file);

    void editarProducto(int id, ProductoEditRequestDTO req);

    // Método para editar producto con imagen
    void editarProducto(int id, ProductoEditRequestDTO req, MultipartFile file);

    void eliminarProducto(int id);

    // Métodos auxiliares para llenar comboboxes
    List<CategoriaProd> listarCategorias();

    List<Marca> listarMarcas();

    List<Map<String, Object>> listarUbicaciones();

    Map<String, Object> crearCategoria(String nombre);

    Map<String, Object> crearMarca(String nombre);

    Map<String, Object> crearUbicacion(String sala, String estante, Integer nivel, String descripcion);
}