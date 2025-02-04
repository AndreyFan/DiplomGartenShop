package de.telran.gartenshop.mapper;

import de.telran.gartenshop.dto.requestDto.CartRequestDto;
import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import de.telran.gartenshop.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class Mappers {
    private ModelMapper modelMapper;

    public Mappers(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

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

    public ProductResponseDto convertToProductResponseDto(ProductEntity productEntity) {
        ProductResponseDto productResponseDto = modelMapper.map(productEntity, ProductResponseDto.class);

        CategoryResponseDto categoryResponseDto = convertToCategoryResponseDto(productEntity.getCategory()); // второй связанный объект
        productResponseDto.setCategory(categoryResponseDto);

        return productResponseDto;
    }

    public ProductEntity convertToProductEntity(ProductRequestDto productRequestDto) {
        ProductEntity productEntity = modelMapper.map(productRequestDto, ProductEntity.class);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(productRequestDto.getCategoryId());
        productEntity.setCategory(categoryEntity);

        return productEntity;
    }

    public FavoriteResponseDto convertToFavoriteResponseDto(FavoriteEntity favorite) {
        FavoriteResponseDto favoriteResponseDto = modelMapper.map(favorite, FavoriteResponseDto.class);
        modelMapper.typeMap(FavoriteEntity.class, FavoriteResponseDto.class)
                .addMappings(mapper -> mapper.skip(FavoriteResponseDto::setUserResponseDto));
        favoriteResponseDto.setProductResponseDto(convertToProductResponseDto(favorite.getProduct()));
        return favoriteResponseDto;
    }
}
