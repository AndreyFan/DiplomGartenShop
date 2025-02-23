package de.telran.gartenshop.controller;


import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.requestDto.UserUpdateDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Users-Endpoint", description = "Controller for working with Users",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
public interface UserControllerInterface {

    @Operation(summary = "User", description = "User")
    public Boolean registerUser(@RequestBody UserRequestDto userRequestDto);

    @Operation(summary = "Admin", description = "Admin")
    public Boolean registerAdmin(@RequestBody UserRequestDto userRequestDto);

    @Operation(summary = "User", description = "User")
    public UserResponseDto getUserById(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable Long userId);

    @Operation(summary = "User", description = "User")
    public UserResponseDto getUserByEmail(@RequestParam String email);

    @Operation(summary = "Update", description = "Update")
    public Boolean updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long userId);

    @Operation(summary = "Delete", description = "Delete")
    public void deleteUser(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable Long userId);


}
