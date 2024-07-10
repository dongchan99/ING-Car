package org.ingcar_boot_war.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.ingcar_boot_war.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();

        // 세션에서 "user_session" 속성이 있는지 확인하여 로그인 여부 판단
        UserDTO user = (UserDTO) session.getAttribute("user_session");
        if (user == null) {
            // 로그인되지 않은 경우
            String alertMessage = "로그인이 필요한 기능입니다. 로그인 페이지로 이동합니다.";
            String script = "<script type='text/javascript'>alert('" + alertMessage + "'); window.location.href='/loginPage';</script>";
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(script);
            return false; // 요청 중지
        }

        // /admin 경로에 대한 추가 검증
        if (request.getRequestURI().startsWith("/admin")) {
            String userRole = user.getUser_role();
            System.out.println("User Role: " + userRole); // 디버깅을 위한 로그 출력
            if (!"1".equals(userRole)) {
                // 권한이 없는 경우
                String alertMessage = "접근 권한이 없습니다. 메인 페이지로 이동합니다.";
                String script = "<script type='text/javascript'>alert('" + alertMessage + "'); window.location.href='/';</script>";
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write(script);
                return false; // 요청 중지
            }
        }

        return true; // 요청 진행
    }
}
