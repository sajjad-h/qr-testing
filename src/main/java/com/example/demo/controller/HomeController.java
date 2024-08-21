package com.example.demo.controller;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.utils.DecryptionUtil;
import com.example.demo.utils.EncryptionUtil;
import com.example.demo.utils.KeyPairGeneratorUtil;
import com.example.demo.utils.QRCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KeyPairGeneratorUtil keyPairGeneratorUtil;

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    // @GetMapping("/generate-qr")
    // public ResponseEntity<String> generateQRCode() {
    // try {
    // qrCodeService.generateQRCodeImage("Your Text Here", 350, 350,
    // "src/main/resources/QRCode.png");
    // return ResponseEntity.ok("QR code generated successfully!");
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
    // occurred: " + e.getMessage());
    // }
    // }

    @PostMapping(value = "/generate-qr", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> generateQRCode(@RequestParam Map<String, String> formData) {
        try {
            // Convert form data to JSON
            String json = objectMapper.writeValueAsString(formData);

            // Encrypt JSON data
            String encryptedData = EncryptionUtil.encrypt(json, keyPairGeneratorUtil.getPublicKey());

            // Generate QR code
            byte[] qrCodeImage = qrCodeService.generateQRCodeImage(encryptedData, 350, 350);

            // Convert byte array to Base64 to embed in HTML
            String base64Image = Base64.getEncoder().encodeToString(qrCodeImage);
            String imgTag = "<img src='data:image/png;base64," + base64Image + "' alt='QR Code'/>";

            return ResponseEntity.ok(imgTag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload-qr")
    public ResponseEntity<String> uploadQRCode(@RequestParam("qrCode") MultipartFile qrCodeFile) {
        try {
            // Convert the uploaded file to a byte array
            byte[] qrCodeBytes = qrCodeFile.getBytes();

            // Decode QR code content (assume it contains encrypted data)
            String encryptedData = qrCodeService.decodeQRCodeImage(qrCodeBytes);

            // Decrypt the data using the private key
            String decryptedData = DecryptionUtil.decrypt(encryptedData, keyPairGeneratorUtil.getPrivateKey());

            // Display decrypted data
            return ResponseEntity.ok("Decrypted data: " + decryptedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
