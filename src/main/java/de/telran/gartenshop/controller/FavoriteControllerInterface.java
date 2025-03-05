package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responsedto.FavoriteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Tag(name = "Favorites", description = "Controller for managing favorites")
@Validated
public interface FavoriteControllerInterface {
    @Operation(summary = "Get all Favorites", description = "Retrieves a list of all favorite items" +
            " for the user",
              security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<FavoriteResponseDto> getAllFavorites();

    @Operation(summary = "Get Favorites by UserId", description = "Request the list of favorite products " +
            "for a user by their userId",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public Set<FavoriteResponseDto> getFavoritesByUserId(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "Get Favorites", description = "Request a list of favorite products for the user by their email",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public Set<FavoriteResponseDto> getFavorites(
            @Parameter(description = "E-Mail", required = true, example = "charlie.brown@example.com")
            @RequestParam
            @Email(message = "Invalid email format. Please provide a valid email.") String email);

    @Operation(summary = "Create a new Favorite", description = "Allows the user to add a new item" +
            " to their list of favorites",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public Boolean createFavorite(@RequestBody @Valid FavoriteRequestDto favoriteRequestDto);

    @Operation(summary = "Delete a Favorite", description = "Removes a favorite based on the" +
            " data provided in the request body",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public Boolean deleteFavorite(@RequestBody @Valid FavoriteRequestDto favoriteRequestDto);
}
