package com.duoc_panol.api_CodigoBarras.service;

public interface BarcodeService {
    byte[] generateBarcodeImage(String code) throws Exception;
}
