package de.telran.gartenshop.mapper;

import de.telran.gartenshop.dto.requestDto.*;
import de.telran.gartenshop.dto.responseDto.CartItemResponseDto;
import de.telran.gartenshop.dto.responseDto.CartResponseDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;

import de.telran.gartenshop.entity.*;
import lombok.RequiredArgsConstructor;

import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import de.telran.gartenshop.entity.*;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mappers {
    private final ModelMapper modelMapper;

    public CategoryResponseDto convertToCategoryResponseDto(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryResponseDto.class);
    }

    public CategoryEntity convertToCategoryEntity(CategoryRequestDto categoryRequestDto) {
        return modelMapper.map(categoryRequestDto, CategoryEntity.class);
    }

    public UserEntity convertToUserEntity(UserRequestDto userRequestDto) {
        return modelMapper.map(userRequestDto, UserEntity.class);
    }

    public CartEntity convertToCartEntity(CartRequestDto cartRequestDto) {
        return modelMapper.map(cartRequestDto, CartEntity.class);
    }

    public CartResponseDto convertToCartResponseDto(CartEntity cartEntity) {
        return modelMapper.map(cartEntity, CartResponseDto.class);
    }

    public ProductResponseDto convertToProductResponseDto(ProductEntity productEntity) {
        ProductResponseDto productResponseDto = modelMapper.map(productEntity, ProductResponseDto.class);
        productResponseDto.setCategoryResponseDto(convertToCategoryResponseDto(productEntity.getCategory())); //связанный объект
        return productResponseDto;
    }

    public ProductEntity convertToProductEntity(ProductRequestDto productRequestDto) {
        ProductEntity productEntity = modelMapper.map(productRequestDto, ProductEntity.class);

        CategoryEntity categoryEntity = new CategoryEntity(); //связанный объект
        categoryEntity.setCategoryId(productRequestDto.getCategoryId());
        productEntity.setCategory(categoryEntity);

        return productEntity;
    }
  
    public CartItemResponseDto convertToCartItemResponseDto(CartItemEntity cartItemEntity) {
        CartItemResponseDto cartItemResponseDto = modelMapper.map(cartItemEntity, CartItemResponseDto.class);
        cartItemResponseDto.setCartResponseDto(convertToCartResponseDto(cartItemEntity.getCart())); //связанный объект
        cartItemResponseDto.setProductResponseDto(convertToProductResponseDto(cartItemEntity.getProduct())); //связанный объект

        return cartItemResponseDto;
    }

    public CartItemEntity convertToCartItemEntity(CartItemRequestDto cartItemRequestDto) {
        return modelMapper.map(cartItemRequestDto, CartItemEntity.class);

    public FavoriteResponseDto convertToFavoriteResponseDto(FavoriteEntity favorite) {
        FavoriteResponseDto favoriteResponseDto = modelMapper.map(favorite, FavoriteResponseDto.class);
        modelMapper.typeMap(FavoriteEntity.class, FavoriteResponseDto.class)
                .addMappings(mapper -> mapper.skip(FavoriteResponseDto::setUserResponseDto));
        favoriteResponseDto.setProductResponseDto(convertToProductResponseDto(favorite.getProduct()));
        return favoriteResponseDto;
    }
}
