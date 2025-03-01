package de.telran.gartenshop.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequestRefreshDto {

    private String refreshToken;

}
