package com.duoc_panol.api_CodigoBarras.service;

import com.duoc_panol.api_CodigoBarras.exception.BarcodeException;
import com.duoc_panol.api_CodigoBarras.util.BarcodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class BarcodeServiceImpl implements BarcodeService {

    @Override
    public byte[] generateBarcodeImage(String code) {
        try {
            return BarcodeGenerator.generateCode128(code, 400, 150);
        } catch (Exception e) {
            throw new BarcodeException("Error al generar el código de barras para: " + code, e);
        }
    }
}
