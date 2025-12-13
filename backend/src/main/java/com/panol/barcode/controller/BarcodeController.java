package com.panol.barcode.controller;

import org.springframework.web.bind.annotation.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

  @PostMapping("/generate")
  public Map<String, String> generarBarcode(@RequestBody Map<String, String> body) {
    try {
      String contenido = body.get("contenido");
      if (contenido == null || contenido.isEmpty()) {
        Map<String,String> result = new HashMap<>();
        result.put("error", "Contenido vacío");
        return result;
      }

      QRCodeWriter writer = new QRCodeWriter();
      BitMatrix bitMatrix = writer.encode(contenido, BarcodeFormat.QR_CODE, 200, 200);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
      byte[] imageBytes = outputStream.toByteArray();

      String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

      Map<String,String> result = new HashMap<>();
      result.put("success", "true");
      result.put("barcode", "data:image/png;base64," + imageBase64);
      return result;
    } catch (Exception e) {
      Map<String,String> result = new HashMap<>();
      result.put("error", "Error al generar código: " + e.getMessage());
      return result;
    }
  }
}
