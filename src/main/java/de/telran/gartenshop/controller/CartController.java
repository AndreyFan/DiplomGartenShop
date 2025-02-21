package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.CartItemRequestDto;
import de.telran.gartenshop.dto.responseDto.CartItemResponseDto;
import de.telran.gartenshop.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
//@Validated
public class CartController implements CartControllerInterface {
    private final CartService cartService;

    //получить все товары во всех корзинах //localhost:8088/cart/get
    @GetMapping(value = "/get")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponseDto> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    //получить товары в корзине определенного пользователя (поиск по userId) //localhost:8088/cart/get/1
    @GetMapping(value = "/get/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<CartItemResponseDto> getAllCartItemsByUserId(
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId) {
        return cartService.getAllCartItemsByUserId(userId);
    }

    //Добавление товара в корзину пользователя (поиск по userId) //localhost:8088/cart/1
    @PostMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto,
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId) {
        return cartService.createCartItem(cartItemRequestDto, userId);
    }

    //Удаление товара в корзине (поиск по cartItemId)  //localhost:8088/cart/1
    @DeleteMapping(value = "/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCartItem(
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long cartItemId) { //delete
        cartService.deleteCartItem(cartItemId);
    }

    //Очистка корзины пользователя (поиск по userId) //localhost:8088/cart/del/1
    @DeleteMapping(value = "/del/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllCartItems(
            @PathVariable
          //  @Min(value = 1, message = "Invalid Id: Id must be >= 1")
              Long userId) {
        cartService.deleteAllCartItems(userId);
    }
}

