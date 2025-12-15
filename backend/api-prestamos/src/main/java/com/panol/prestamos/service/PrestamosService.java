package com.panol.prestamos.service;

import com.panol.prestamos.dto.*;
import java.util.List;

public interface PrestamosService {
    List<PrestamoResponseDTO> listarPrestamos();
    void prestar(PrestarRequestDTO req);
    void devolver(DevolverRequestDTO req);
}