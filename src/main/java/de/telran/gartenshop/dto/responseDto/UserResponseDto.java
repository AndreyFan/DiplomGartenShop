package de.telran.gartenshop.dto.responseDto;

import de.telran.gartenshop.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private Role role;
}
