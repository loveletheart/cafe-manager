package myapp.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import myapp.entity.UserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeloginService {

    private static final String QR_CODE_PATH = "src/main/resources/static/qr_codes/";
    
    @Autowired
    public QRTokenService qrTokenService;
    
    //회원가입시 QR코드 자동생성
    public String generateQRCode(String id,String baseUrl) {
        try {
            String token = qrTokenService.createToken(id); // 토큰 생성
            String qrLoginUrl = baseUrl + "/QRredirect?token=" + token;
            
            String qrFileName = id + "_qr.png";
            String filePath = QR_CODE_PATH + qrFileName;
            
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(qrLoginUrl, BarcodeFormat.QR_CODE, 200, 200, hints);
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return "/qr_codes/" + qrFileName; // 상대 경로 반환
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
