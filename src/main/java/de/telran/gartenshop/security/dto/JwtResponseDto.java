package de.telran.gartenshop.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponseDto {
    @Schema(description = "Bearer", example = "Bearer")
    private final String type = "Bearer";

    private String accessToken;
    private String refreshToken;
}