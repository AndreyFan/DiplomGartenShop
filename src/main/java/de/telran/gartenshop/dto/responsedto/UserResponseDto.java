package de.telran.gartenshop.dto.responsedto;

import de.telran.gartenshop.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    @Schema(description = "Identifier", example = "3")
    private Long userId;

    @Schema(description = "Name", example = "Charlie Brown")
    private String name;

    @Schema(description = "E-Mail", example = "charlie.brown@example.com")
    private String email;

    @Schema(description = "Phone Number", example = "1234567892")
    private String phone;

    @Schema(description = "PasswordHash", example = "$2a$10$OBp3oiPZBEoMoTXfN2IJie.GYqh1RgkFsLovoLJ1UPSLB2YMWL4Ji")
    private String passwordHash;

    @Schema(description = "Role", example = "CLIENT")
    private Role role;

    @Schema(description = "refreshToken", example = "192837465")
    private String refreshToken;
}
