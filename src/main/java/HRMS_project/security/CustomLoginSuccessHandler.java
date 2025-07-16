package HRMS_project.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectURL = request.getContextPath();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                redirectURL += "/adm/dashboard";
            	//redirectURL += "/admin/registers";
                break;
            } else if (role.equals("ROLE_HR_MANAGER")) {
                redirectURL += "/hr/dashboard";
                break;
            } else if (role.equals("ROLE_EMPLOYEE")) {
                redirectURL += "/emp/dashboard";
                break;
            } else if (role.equals("ROLE_MANAGER")) {
                redirectURL += "/mng/dashboard";
                break;
            } else if (role.equals("ROLE_CANDIDATE")) {
                redirectURL += "/cnd/dashboard";
                break;
            }
        }

        if (redirectURL.equals(request.getContextPath())) {
            redirectURL += "/access-denied"; // fallback if no role matched
        }

        response.sendRedirect(redirectURL);
    }
}
