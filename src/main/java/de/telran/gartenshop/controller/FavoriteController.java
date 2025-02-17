package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import de.telran.gartenshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // запрос списка товаров-фаворитов для юзера (по его userId)
    // http://localhost:8088/favorites/3   - список фаворитов для юзера с userId=3
    @GetMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<FavoriteResponseDto> getFavoritesByUserId(@PathVariable Long userId){
        return favoriteService.getFavoritesByUserId(userId);
    }

    // запрос списка товаров-фаворитов для юзера (по его email)
    // пример запроса: http://localhost:8088/favorites/get?email=henry.lewis@example.com
    @GetMapping(value = "/get")
    @ResponseStatus(HttpStatus.OK)
    public Set<FavoriteResponseDto> getFavorites(@RequestParam String email){
        return favoriteService.getFavorites(email);
    }

    // добавление товара в избранное (фавориты)
    // http://localhost:8088/favorites
    // тело запроса
    // {
    //    "productId": 5,
    //    "userId": 3
    //}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean createFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto){
        return favoriteService.createFavorite(favoriteRequestDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto){
        return favoriteService.deleteFavorite(favoriteRequestDto);
    }

}
