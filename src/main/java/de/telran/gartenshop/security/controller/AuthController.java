package de.telran.gartenshop.security.controller;

import de.telran.gartenshop.security.dto.JwtRequestDto;
import de.telran.gartenshop.security.dto.JwtRequestRefreshDto;
import de.telran.gartenshop.security.dto.JwtResponseDto;
import de.telran.gartenshop.security.service.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerInterface{

    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest) throws AuthException {
        final JwtResponseDto token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody JwtRequestRefreshDto request) throws AuthException {
        final JwtResponseDto token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> getNewRefreshToken(@RequestBody JwtRequestRefreshDto request) throws AuthException {
        final JwtResponseDto token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
