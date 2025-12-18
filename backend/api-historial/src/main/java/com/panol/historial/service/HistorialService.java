package com.panol.historial.service;

import com.panol.historial.dto.HistorialCreateRequestDTO;
import com.panol.historial.dto.HistorialRow;
import java.util.List;

/**
 * Servicio para la gestión del historial de préstamos.
 */
public interface HistorialService {

    /**
     * Obtiene todos los registros del historial como una lista.
     * La adición de enlaces HATEOAS se realiza en el controlador.
     */
    List<HistorialRow> listarHistorial();

    /** Busca un registro de historial por su identificador. */
    HistorialRow buscarPorId(int idDetalle);

    /**
     * Edita un registro de historial. Solo se actualizan los campos presentes en el
     * DTO.
     * 
     * @return el DTO actualizado.
     */
    HistorialRow editarHistorial(int idDetalle, HistorialRow dto);

    /** Elimina un registro del historial. */
    void eliminarHistorial(int idDetalle);

    HistorialRow crearHistorial(HistorialCreateRequestDTO dto);

}
