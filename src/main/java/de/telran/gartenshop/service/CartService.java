package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestdto.CartItemRequestDto;
import de.telran.gartenshop.dto.responsedto.CartItemResponseDto;
import de.telran.gartenshop.entity.*;
import de.telran.gartenshop.exception.DataNotFoundInDataBaseException;
import de.telran.gartenshop.exception.UserNotFoundException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CartItemRepository;
import de.telran.gartenshop.repository.ProductRepository;
import de.telran.gartenshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserRepository userRepository;
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
            throw new UserNotFoundException("User with Id: " + userId + " not found.");
        }
    }

    //добавление товара в корзину
    public boolean createCartItem(CartItemRequestDto cartItemRequestDto, Long userId) {
        CartItemEntity createCartItemEntity = mappers.convertToCartItemEntity(cartItemRequestDto);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity != null) {
            ProductEntity productEntity = productRepository.findById(cartItemRequestDto.getProductId()).orElse(null);
            if (productEntity == null) {
                throw new DataNotFoundInDataBaseException("Product with Id " + cartItemRequestDto.getProductId() + " not found and not added to the Cart");
            }

            createCartItemEntity.setCartItemId(null);

            createCartItemEntity.setCart(userEntity.getCart());

            createCartItemEntity.setProduct(productEntity);

            CartItemEntity savedCartItemEntity = cartItemRepository.save(createCartItemEntity);
            return savedCartItemEntity.getCartItemId() != null;
        } else {
            throw new UserNotFoundException("User not found with Id: " + userId);
        }
    }

    public void deleteCartItem(Long cartItemId) {
        CartItemEntity deleteCartItemEntity = cartItemRepository.findById(cartItemId).orElse(null);
        if (deleteCartItemEntity != null) {
            cartItemRepository.delete(deleteCartItemEntity);
        } else {
            throw new DataNotFoundInDataBaseException("CartItem not found with Id: " + cartItemId);
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
            throw new UserNotFoundException("User not found with Id: " + userId);
        }
    }
}
