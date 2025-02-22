package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Tag(name = "Favorites-Endpoint", description = "Controller for managing favorites",
        externalDocs = @ExternalDocumentation(description = "Link to external documentation in German",
                url = "https://gartenshopExDoc.de"
        )
)
public interface FavoriteControllerInterface {

    @Operation(summary = "Get Favorites by UserId", description = "Request the list of favorite products " +
            "for a user by their userId")
    public Set<FavoriteResponseDto> getFavoritesByUserId(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable Long userId);

    @Operation(summary = "Get Favorites", description = "Request a list of favorite products for the user by their email")
    public Set<FavoriteResponseDto> getFavorites(@RequestParam String email);

    @Operation(summary = "Create a new Favorite", description = "Allows the user to add a new item" +
            " to their list of favorites")
    public Boolean createFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto);

    @Operation(summary = "Delete a Favorite", description = "Removes a favorite based on the" +
            " data provided in the request body")
    public Boolean deleteFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto);
}
