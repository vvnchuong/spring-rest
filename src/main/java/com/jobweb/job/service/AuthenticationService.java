package com.jobweb.job.service;

import com.jobweb.job.domain.InvalidateToken;
import com.jobweb.job.domain.User;
import com.jobweb.job.domain.dto.request.AuthenticationRequest;
import com.jobweb.job.domain.dto.request.IntrospectRequest;
import com.jobweb.job.domain.dto.request.LogoutRequest;
import com.jobweb.job.domain.dto.request.RefreshRequest;
import com.jobweb.job.domain.dto.response.AuthenticationResponse;
import com.jobweb.job.domain.dto.response.IntrospectResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.repository.InvalidateTokenRepository;
import com.jobweb.job.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final InvalidateTokenRepository invalidateTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public AuthenticationService(UserRepository userRepository,
                                 InvalidateTokenRepository invalidateTokenRepository){
        this.userRepository = userRepository;
        this.invalidateTokenRepository = invalidateTokenRepository;
    }

    // kiem tra token
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
        boolean isValid = true;

        try{
            verifyToken(token, false);
        }catch (RuntimeException e){
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date())))
            throw new RuntimeException();

        if(invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new RuntimeException();

        return signedJWT;
    }

    // xac thuc (dang nhap)
    public AuthenticationResponse authenticationResponse(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException());

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated)
            throw new RuntimeException();

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    // logout token
    public void logoutToken(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(), true);

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        this.invalidateTokenRepository.save(invalidateToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidatedToken =
                InvalidateToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidateTokenRepository.save(invalidatedToken);

        var email = signedJWT.getJWTClaimsSet().getIssuer();

        var user =
                userRepository.findUserByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getName())
                .issuer(user.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            throw new RuntimeException();
        }
    }

}
