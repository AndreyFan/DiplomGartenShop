package de.telran.gartenshop.controller;


import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.requestDto.UserUpdateDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Users-Endpoint", description = "Controller for working with Users",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
@Validated
public interface UserControllerInterface {

    @Operation(summary = "User", description = "User")
    public Boolean registerUser(@RequestBody @Valid UserRequestDto userRequestDto);

    @Operation(summary = "Admin", description = "Admin")
    public Boolean registerAdmin(@RequestBody @Valid UserRequestDto userRequestDto);

    @Operation(summary = "User", description = "User")
    public UserResponseDto getUserById(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "User", description = "User")
    public UserResponseDto getUserByEmail(
            @RequestParam
            @Email(message = "Invalid email format. Please provide a valid email.") String email);

    @Operation(summary = "Update", description = "Update")
    public Boolean updateUser(
            @RequestBody @Valid UserUpdateDto userUpdateDto,
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "Delete", description = "Delete")
    public void deleteUser(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);


}
