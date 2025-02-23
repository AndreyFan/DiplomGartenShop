package de.telran.gartenshop.security.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequestDto {
    private String email;
    private String password;
}
