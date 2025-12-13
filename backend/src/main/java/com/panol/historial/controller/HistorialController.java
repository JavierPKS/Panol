package com.panol.historial.controller;

import com.panol.historial.dto.HistorialRow;
import com.panol.historial.repository.HistorialRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

  private final HistorialRepository repo;

  public HistorialController(HistorialRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<HistorialRow> general() {
    return repo.general();
  }
}
