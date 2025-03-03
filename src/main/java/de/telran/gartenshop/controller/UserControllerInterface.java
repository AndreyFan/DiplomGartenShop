package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.UserRequestDto;
import de.telran.gartenshop.dto.requestdto.UserUpdateDto;
import de.telran.gartenshop.dto.responsedto.UserResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Hidden;
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

@Tag(name = "Users", description = "Controller for working with Users",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
@Validated
public interface UserControllerInterface {

    @Operation(summary = "Register a new user", description = "This endpoint allows a new user (Client) to register" +
            " by providing necessary information such as name, email, phone, and password")
    public Boolean registerUser(
            @Parameter(description = "User details for registration", required = true)
            @RequestBody @Valid UserRequestDto userRequestDto);

    @Operation(summary = "Register a new admin", description = "This endpoint allows the registration of a new admin" +
            " user by providing necessary information such as name, email, phone, and password.")
    public Boolean registerAdmin(
            @Parameter(description = "Admin details for registration", required = true)
            @RequestBody @Valid UserRequestDto userRequestDto);

    @Operation(summary = "Get user details by user ID", description = "This endpoint retrieves the details of a specific" +
            " user by their unique user ID")
    public UserResponseDto getUserById(
            @Parameter(description = "Identifier", required = true, example = "3")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "Get user details by email", description = "This endpoint allows you to search for a user by" +
            " their email address and retrieve their details")
    public UserResponseDto getUserByEmail(
            @Parameter(description = "The email of the user to retrieve", required = true, example = "charlie.brown@example.com")
            @RequestParam
            @Email(message = "Invalid email format. Please provide a valid email.") String email);

    @Operation(summary = "Update user details by user ID", description = "This endpoint allows the updating of a user's " +
            "details by providing the user ID and the updated user information")
    public Boolean updateUser(
            @Parameter(description = "The details of the user to update", required = true)
            @RequestBody @Valid UserUpdateDto userUpdateDto,
            @Parameter(description = "Identifier", required = true, example = "3")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Hidden
    @Operation(summary = "Delete user by user ID", description = "This endpoint allows the deletion of a user by " +
            "their unique user ID")
    public void deleteUser(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);
}
