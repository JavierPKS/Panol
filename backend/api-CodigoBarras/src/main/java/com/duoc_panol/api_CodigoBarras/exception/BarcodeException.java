package com.duoc_panol.api_CodigoBarras.exception;

public class BarcodeException extends RuntimeException {

    public BarcodeException(String message) {
        super(message);
    }

    public BarcodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
