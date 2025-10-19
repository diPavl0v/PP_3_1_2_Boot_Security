package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import javax.servlet.http.*; import javax.servlet.ServletException; import java.io.IOException;

public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                        Authentication auth) throws IOException, ServletException {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        setDefaultTargetUrl(admin ? "/admin" : "/user");
        super.onAuthenticationSuccess(req, res, auth);
    }
}
