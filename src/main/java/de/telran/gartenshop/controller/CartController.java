package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.CartItemRequestDto;
import de.telran.gartenshop.dto.responseDto.CartItemResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping(value = "/get")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponseDto> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    @GetMapping(value = "/get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<CartItemResponseDto> getAllCartItemsByUserId(@PathVariable Long userId) {
        return cartService.getAllCartItemsByUserId(userId);
    }

    //Добавление товара в корзину
    @PostMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createCartItem(@RequestBody CartItemRequestDto cartItemRequestDto, @PathVariable Long userId) {
        return cartService.createCartItem(cartItemRequestDto, userId);
    }

    @DeleteMapping(value = "/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartItem(@PathVariable Long cartItemId) { //delete
        cartService.deleteCartItem(cartItemId);
    }
}

