package com.jobweb.job.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomJwtDecoder customJwtDecoder;

    private final String[] SIGNED_KEY = {"/api/v1/users", "/api/v1/auth/login",
            "/api/v1/auth/introspect", "/api/v1/auth/logout", "/api/v1/auth/refresh",
            "/api/v1/companies"};

    public SecurityConfig(CustomJwtDecoder customJwtDecoder){
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain httpFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, SIGNED_KEY).permitAll()
                                .anyRequest().authenticated());

        httpSecurity
                .oauth2ResourceServer(oath2 ->
                        oath2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
