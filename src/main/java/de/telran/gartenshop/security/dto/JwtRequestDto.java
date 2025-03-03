package de.telran.gartenshop.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequestDto {
    @Schema(description = "E-Mail", example = "bob.johnson@example.com")
    private String email;

    @Schema(description = "Password", example = "12345")
    private String password;
}
