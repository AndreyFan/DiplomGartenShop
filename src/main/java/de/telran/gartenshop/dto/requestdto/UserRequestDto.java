package de.telran.gartenshop.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @Schema(description = "Name", example = "Bob Johnson")
    @NotBlank(message = "Invalid name: Empty name")
    @Size(min = 2, max = 50, message = "Invalid name: Name must be of 2 - 50 characters")
    private String name;

    @Schema(description = "E-Mail", example = "bob.johnsonAndJohnson@example.com")
    @NotBlank(message = "Invalid email: Empty email")
    @Email(message = "Invalid email")
    private String email;

    @Schema(description = "Phone", example = "1234567891")
    @NotBlank(message = "Invalid Phone number: Empty number")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number: Phone number must be 10 digits long.")
    private String phone;  //10 digits without symbols and spaces

    @Schema(description = "Password", example = "12345")
    @NotBlank(message = "Invalid password: Empty password")
    @Size(min = 5, max = 15, message = "Invalid password: Password must be of 5 - 15 characters")
    private String password;
}
