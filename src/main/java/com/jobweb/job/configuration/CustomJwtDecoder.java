package com.jobweb.job.configuration;

import com.jobweb.job.domain.dto.request.IntrospectRequest;
import com.jobweb.job.domain.dto.response.IntrospectResponse;
import com.jobweb.job.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Value("${jwt.signerKey}")
    private String signerKey;

    public CustomJwtDecoder(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectResponse response = this
                    .authenticationService.introspect(IntrospectRequest
                            .builder()
                            .token(token)
                            .build());
            if (!response.isValid())
                throw new JwtException("Token invalid");
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        if(nimbusJwtDecoder == null){
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            this.nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return this.nimbusJwtDecoder.decode(token);
    }
}
