package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.CartItemRequestDto;
import de.telran.gartenshop.dto.responsedto.CartItemResponseDto;
import de.telran.gartenshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController implements CartControllerInterface {
    private final CartService cartService;

    //получить все товары во всех корзинах //localhost:8088/cart/get
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping(value = "/get")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponseDto> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    //получить товары в корзине определенного пользователя (поиск по userId) //localhost:8088/cart/get/1
    @GetMapping(value = "/get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<CartItemResponseDto> getAllCartItemsByUserId(@PathVariable Long userId) {
        return cartService.getAllCartItemsByUserId(userId);
    }

    //Добавление товара в корзину пользователя (поиск по userId) //localhost:8088/cart/1
    @Override
    @PostMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createCartItem(@RequestBody CartItemRequestDto cartItemRequestDto, @PathVariable Long userId) {
        return cartService.createCartItem(cartItemRequestDto, userId);
    }

    //Удаление товара в корзине (поиск по cartItemId)  //localhost:8088/cart/1
    @DeleteMapping(value = "/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }

    //Очистка корзины пользователя (поиск по userId) //localhost:8088/cart/del/1
    @Override
    @DeleteMapping(value = "/del/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllCartItems(@PathVariable Long userId) {
        cartService.deleteAllCartItems(userId);
    }
}

