package com.example.roommate.controllers.start;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MySimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) throws IOException {

    }

    /**
     * .
     * @param authentication Informationen der Nutzer
     * @return userHome oder adminHome
     */
    public static String determineTargetUrl(
            final Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();
        Set<String> collect = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (collect.contains("ROLE_ADMIN")) {
            return "/adminHome";
        }
        return "/userHome";
    }
}