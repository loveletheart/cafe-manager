package myapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myapp.entity.UserData;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserData user) {  // UserData로 변환 가능
            if ("master".equals(user.getId())) {  // ID 기반 비교
                setDefaultTargetUrl("/admin");
            } else {
                setDefaultTargetUrl("/menu");
            }
        } else {
            setDefaultTargetUrl("/menu");
            System.out.println("UserData가 아님, 기본 /menu로 이동");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
    //업데이트 테스트3
}
