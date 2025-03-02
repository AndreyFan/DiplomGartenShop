package de.telran.gartenshop.security.controller;

import de.telran.gartenshop.security.dto.JwtRequestDto;
import de.telran.gartenshop.security.dto.JwtRequestRefreshDto;
import de.telran.gartenshop.security.dto.JwtResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication", description = "Controller for authentication",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
public interface AuthControllerInterface {
    @Operation(summary = "User login", description = "This endpoint allows a user to log in by providing valid " +
            "credentials (email and password) and receive a JWT token")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest) throws AuthException;

    @Operation(summary = "Get a new access token", description = "This endpoint allows a user to obtain a new access " +
            "token by providing a valid refresh token")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody JwtRequestRefreshDto request) throws AuthException;

    @Operation(summary = "Refresh the refresh token", description = "This endpoint allows a user to obtain a new refresh " +
            "token by providing the current refresh token")
    public ResponseEntity<JwtResponseDto> getNewRefreshToken(@RequestBody JwtRequestRefreshDto request) throws AuthException;

}
