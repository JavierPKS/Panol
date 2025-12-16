package com.panol.historial.service;

import com.panol.historial.dto.HistorialRow;
import java.util.List;

public interface HistorialService {
    List<HistorialRow> obtenerHistorialGeneral();
}