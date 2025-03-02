package de.telran.gartenshop.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Schema(description = "Name", example = "David John")
    @NotBlank(message = "Invalid name: Empty name")
    @Size(min = 2, max = 50, message = "Invalid name: Name must be of 2 - 50 characters")
    private String name;

    @Schema(description = "Phone", example = "4915564745")
    @NotBlank(message = "Invalid Phone number: Empty number")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number: Phone number must be 10 digits long.") //10 цифр без символов и пробелов
    private String phone;
}
