package com.example.roommate.config.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class SecurityConfig {

    /**
     * .
     */
    @Value("${roommate.rollen.admin}")
    private Set<String> admins;

    /**
     * .
     * @param chainBuilder Instanz Variable
     * @return ein SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain configure(
            final HttpSecurity chainBuilder) throws Exception {
        chainBuilder.authorizeHttpRequests(
                        configurer -> configurer.requestMatchers(
                                "/", "/error", "/css/*",
                                        "/img/*", "/js/*").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(c -> c.csrfTokenRepository(
                        CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED)))
                .logout(l -> l.logoutSuccessUrl("/").permitAll())
                .oauth2Login(Customizer.withDefaults());
        return chainBuilder.build();
    }

    /**
     * .
     * @return ein alle User mit ihr Authority
     */
   @Bean
   OAuth2UserService<OAuth2UserRequest, OAuth2User> createUserService() {
        DefaultOAuth2UserService defaultOAuth2UserService =
                new DefaultOAuth2UserService();
        return userRequest -> {
            OAuth2User oAuth2User =
                    defaultOAuth2UserService.loadUser(userRequest);

            var attributes = oAuth2User.getAttributes();
            var authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            String login = attributes.get("login").toString();

            if (admins.contains(login)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                System.out.printf(
                        "DENYING ADMIN PRIVILEGES TO USER %s%n", login);
            }

            return new DefaultOAuth2User(authorities, attributes, "login");
        };
    }

}
