package com.example.demo.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import com.google.zxing.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;


import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    public byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }


    public String decodeQRCodeImage(byte[] qrCodeImage) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCodeImage);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        QRCodeReader reader = new QRCodeReader();
        
        Result result = reader.decode(bitmap);
        return result.getText();
    }
}
