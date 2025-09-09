package myapp.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

@Service
public class QRCodeloginService {
    
    @Autowired
    public QRTokenService qrTokenService;
    

    @Value("${app.qr.storage-path}")
    private String qrCodeStoragePath;
    
    //회원가입시 QR코드 자동생성
    public String generateQRCode(String qrToken, String baseUrl) {
        try {
            String qrLoginUrl = baseUrl + "/qr-login?token=" + qrToken;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(qrLoginUrl, BarcodeFormat.QR_CODE, 200, 200, hints);

            // BitMatrix → BufferedImage
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // BufferedImage → Base64 변환
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

            return "data:image/png;base64," + base64Image;

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}