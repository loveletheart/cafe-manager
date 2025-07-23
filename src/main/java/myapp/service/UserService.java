package myapp.service;

import myapp.entity.UserData;
import myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QRCodeloginService qrCodeloginService; // QR 토큰 서비스 추가

    // 로그인 시 호출됨. 전달받은 ID를 이용하여 사용자 정보 조회
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        System.out.println("서비스에서 받은 ID 값: " + id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }
    
    //qr코드로 로그인시 사용사 정보 조회
    public UserData findUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // 회원가입 시 QR 코드 자동 생성
    public boolean registerUser(String id, String password, String username, String role,String baseUrl) {
        if (userRepository.findById(id).isPresent()) {
            return false; // 이미 존재하는 ID
        }

        String encodedPassword = passwordEncoder.encode(password);
        String qrToken = qrCodeloginService.generateQRCode(id,baseUrl); // QR 토큰 생성

        UserData newUser = new UserData(id, username, encodedPassword, role, qrToken);
        userRepository.save(newUser);
        return true;
    }
}
