package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responsedto.FavoriteResponseDto;
import de.telran.gartenshop.security.jwt.JwtAuthentication;
import de.telran.gartenshop.service.FavoriteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/favorites")
public class FavoriteController implements FavoriteControllerInterface {
    private final FavoriteService favoriteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public List<FavoriteResponseDto> getAllFavorites(){
        return favoriteService.getAllFavorites();
    }

    // request a list of favorite products for a user (by his userId)
    // http://localhost:8088/favorites/3   - list of favorites for user with userId=3
    @GetMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<FavoriteResponseDto> getFavoritesByUserId(@PathVariable Long userId) {
        return favoriteService.getFavoritesByUserId(userId);
    }

    // request a list of favorite products for a user (by his email)
    // example request: http://localhost:8088/favorites/get?email=henry.lewis@example.com
    @GetMapping(value = "/get")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Set<FavoriteResponseDto> getFavorites() {
        final JwtAuthentication jwtInfoToken = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String email = jwtInfoToken.getEmail();  // достаем email из токена
        return favoriteService.getFavorites(email);
    }

    // adding a product to favorites
    // http://localhost:8088/favorites
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean createFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        return favoriteService.createFavorite(favoriteRequestDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto) {
        return favoriteService.deleteFavorite(favoriteRequestDto);
    }
}
