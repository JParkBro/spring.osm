package com.example.onlineshoppingmall.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomAuthenticationSuccessHandler() {
        // 기본 URL 설정
        setDefaultTargetUrl("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // 명시적 리다이렉트 URL이 있는지 확인
        String redirectUrl = request.getParameter("redirect_url");

        // 명시적 리다이렉트 URL이 있고 유효한 경우 사용
        if (redirectUrl != null && !redirectUrl.isEmpty() && isValidRedirectUrl(redirectUrl)) {
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            return;
        }

        // 없으면 SavedRequestAwareAuthenticationSuccessHandler의 기본 동작 사용
        // (이것이 보호된 페이지로의 리다이렉션을 처리)
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean isValidRedirectUrl(String url) {
        // 상대 경로는 항상 허용
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }

        // 절대 경로인 경우, 허용된 도메인 확인
        String[] allowedDomains = {"localhost"};

        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            if (host != null) {
                for (String domain : allowedDomains) {
                    if (host.equals(domain) || host.endsWith("." + domain)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}