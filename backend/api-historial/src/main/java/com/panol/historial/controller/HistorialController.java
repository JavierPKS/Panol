package com.panol.historial.controller;

import com.panol.historial.dto.HistorialRow;
import com.panol.historial.service.HistorialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

  private final HistorialService service;

  public HistorialController(HistorialService service) {
    this.service = service;
  }

  @GetMapping
  public List<HistorialRow> general() {
    return service.obtenerHistorialGeneral();
  }
}