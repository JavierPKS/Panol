package com.duoc_panol.api_CodigoBarras.controller;

import com.duoc_panol.api_CodigoBarras.service.BarcodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barcodes")
public class BarcodeController {

    private final BarcodeService barcodeService;

    public BarcodeController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    // GET /api/barcodes/{code}
    @GetMapping("/{code}")
    public ResponseEntity<byte[]> generarCodigo(@PathVariable String code) {
        try {
            byte[] image = barcodeService.generateBarcodeImage(code);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=barcode.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
