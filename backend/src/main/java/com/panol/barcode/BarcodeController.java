package com.panol.barcode;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

  @GetMapping(value="/{code}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> generar(@PathVariable String code) throws Exception {
    BitMatrix matrix = new Code128Writer().encode(code, BarcodeFormat.CODE_128, 400, 120);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(matrix, "PNG", baos);

    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(baos.toByteArray());
  }
}
