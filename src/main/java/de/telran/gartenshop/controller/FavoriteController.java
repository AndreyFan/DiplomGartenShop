package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import de.telran.gartenshop.entity.FavoriteEntity;
import de.telran.gartenshop.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // запрос списка товаров-фаворитов для юзера (по его userId)
    @GetMapping(value = "/{userId}")
    public Set<FavoriteResponseDto> getFavoritesByUserId(@PathVariable Long userId){
        return favoriteService.getFavoritesByUserId(userId);
    }

    // запрос списка товаров-фаворитов для юзера (по его email)
    // пример запроса: http://localhost:8088/favorites/get?email=henry.lewis@example.com
    @GetMapping(value = "/get")
    public Set<FavoriteResponseDto> getFavorites(@RequestParam String email){
        return favoriteService.getFavorites(email);
    }

    @PostMapping
    public Boolean createFavorite(@RequestBody FavoriteRequestDto favoriteRequestDto){
        return favoriteService.createFavorite(favoriteRequestDto);
    }

}
