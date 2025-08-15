package com.jobweb.job.controller;

import com.jobweb.job.domain.dto.request.AuthenticationRequest;
import com.jobweb.job.domain.dto.request.IntrospectRequest;
import com.jobweb.job.domain.dto.request.LogoutRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.AuthenticationResponse;
import com.jobweb.job.domain.dto.response.IntrospectResponse;
import com.jobweb.job.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.authenticationResponse(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(
            @RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/user/logout")
    public ApiResponse<Void> logout(
            @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        this.authenticationService.logoutToken(request);
        return ApiResponse.<Void>builder().build();
    }

}
