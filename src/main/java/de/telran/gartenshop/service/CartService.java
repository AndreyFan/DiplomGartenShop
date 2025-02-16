package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.CartItemRequestDto;
import de.telran.gartenshop.dto.responseDto.CartItemResponseDto;
import de.telran.gartenshop.entity.*;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CartItemRepository;
import de.telran.gartenshop.repository.CartRepository;
import de.telran.gartenshop.repository.ProductRepository;
import de.telran.gartenshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final Mappers mappers;

    public List<CartItemResponseDto> getAllCartItems() {
        List<CartItemEntity> cartItemEntityList = cartItemRepository.findAll();
        return MapperUtil.convertList(cartItemEntityList, mappers::convertToCartItemResponseDto);
    }

    public Set<CartItemResponseDto> getAllCartItemsByUserId(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        if (userEntity != null) {
            Set<CartItemEntity> cartItemEntitySet = userEntity.getCart().getCartItems();

            return MapperUtil.convertSet(cartItemEntitySet, mappers::convertToCartItemResponseDto);
        } else {
            throw new IllegalArgumentException("User with Id: " + userId + " not found.");
        }
    }

    //добавление товара в корзину
    public boolean createCartItem(CartItemRequestDto cartItemRequestDto, Long userId) {
        CartItemEntity createCartItemEntity = mappers.convertToCartItemEntity(cartItemRequestDto);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        ProductEntity productEntity = productRepository.findById(cartItemRequestDto.getProductId()).orElse(null);
        if (productEntity == null) {
            throw new HttpMessageConversionException("Product with Id " + cartItemRequestDto.getProductId() + " not added");
        }

        createCartItemEntity.setCartItemId(null);
        createCartItemEntity.setCart(userEntity.getCart());
        createCartItemEntity.setProduct(productEntity);

        CartItemEntity savedCartItemEntity = cartItemRepository.save(createCartItemEntity);

        if (savedCartItemEntity == null) {
            throw new HttpMessageConversionException("Product with Id " + cartItemRequestDto.getProductId() + " not added");
        }
        return savedCartItemEntity.getCartItemId() != null;
    }

    public void deleteCartItem(Long cartItemId) {
        CartItemEntity deleteCartItemEntity = cartItemRepository.findById(cartItemId).orElse(null);
        if (deleteCartItemEntity != null) {
            cartItemRepository.delete(deleteCartItemEntity);
        } else {
            throw new NullPointerException("CartItem not found with Id: " + cartItemId);
        }
    }

    public void deleteAllCartItems(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            CartEntity cartEntity = userEntity.getCart();
            Set<CartItemEntity> cartItemEntitySet = cartEntity.getCartItems();

            for (CartItemEntity cartItem : cartItemEntitySet) {
                cartItemRepository.delete(cartItem);
            }
        } else {
            throw new NullPointerException("User not found with Id: " + userId);
        }
    }
}
