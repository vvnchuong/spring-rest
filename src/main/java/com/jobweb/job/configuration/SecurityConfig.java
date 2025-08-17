package com.jobweb.job.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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
                .cors(cors -> {})
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, SIGNED_KEY).permitAll()
                                .requestMatchers("/storage/**").permitAll()
                                .anyRequest().authenticated());

        httpSecurity
                .oauth2ResourceServer(oath2 ->
                        oath2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                "http://localhost:4173", "http://localhost:5173"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
