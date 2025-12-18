package com.duoc_panol.api_CodigoBarras.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;

public class BarcodeGenerator {

    /**
     * Genera un código de barras en formato PNG a partir de un string
     * @param code Texto a codificar
     * @param width Ancho de la imagen
     * @param height Alto de la imagen
     * @return byte[] con la imagen en PNG
     * @throws Exception si ocurre un error en la generación
     */
    public static byte[] generateCode128(String code, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter()
                .encode(code, BarcodeFormat.CODE_128, width, height);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return baos.toByteArray();
        }
    }
}
