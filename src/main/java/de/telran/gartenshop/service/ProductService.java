package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CategoryRepository;
import de.telran.gartenshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Mappers mappers;

    public List<ProductResponseDto> getAllProducts() {
        List<ProductEntity> productEntityList = productRepository.findAll();
        return MapperUtil.convertList(productEntityList, mappers::convertToProductResponseDto);
    }

    public List<ProductResponseDto> getProducts(Long categoryId, Double minPrice, Double maxPrice,
                                                Boolean isDiscount, String sort) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);

        List<ProductEntity> productEntity = productRepository.findProductByFilter(categoryEntity, minPrice, maxPrice,
                isDiscount, sort);
        List<ProductResponseDto> productResponseDtoList = MapperUtil.convertList(productEntity, mappers::convertToProductResponseDto);
        return productResponseDtoList;

    }

    public boolean createProduct(ProductRequestDto productRequestDto) {
        ProductEntity createProductEntity = mappers.convertToProductEntity(productRequestDto);
        CategoryEntity categoryEntity = categoryRepository.findById(productRequestDto.getCategoryId()).orElse(null);

        Timestamp timestamp = new Timestamp(new Date().getTime());

        createProductEntity.setProductId(null);

        createProductEntity.setCreatedAt(timestamp);
        createProductEntity.setUpdatedAt(timestamp);
        createProductEntity.setCategory(categoryEntity);

        ProductEntity savedProductEntity = productRepository.save(createProductEntity);

        if (savedProductEntity == null) {
            throw new HttpMessageConversionException("Product " + productRequestDto.getName() + " not created");
        }
        return savedProductEntity.getProductId() != null;
    }

    public ProductResponseDto updateProduct(ProductRequestDto productRequestDto, Long productId) {
        ProductEntity updateProductEntity = productRepository.findById(productId).orElse(null);
        CategoryEntity categoryEntity = categoryRepository.findById(productRequestDto.getCategoryId()).orElse(null);

        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (updateProductEntity != null) {
            updateProductEntity.setName(productRequestDto.getName());
            updateProductEntity.setDescription(productRequestDto.getDescription());
            updateProductEntity.setPrice(productRequestDto.getPrice());
            updateProductEntity.setCategory(categoryEntity);
            updateProductEntity.setImageUrl(productRequestDto.getImageUrl());
            updateProductEntity.setDiscountPrice(productRequestDto.getDiscountPrice());
            updateProductEntity.setUpdatedAt(timestamp);
            productRepository.save(updateProductEntity);
        } else {
            throw new NullPointerException("Product not found with Id: " + productId);
        }
        return mappers.convertToProductResponseDto(updateProductEntity);
    }

    public void deleteProduct(Long productId) {
        ProductEntity deleteProductEntity = productRepository.findById(productId).orElse(null);
        if (deleteProductEntity != null) {
            productRepository.delete(deleteProductEntity);
        } else {
            throw new NullPointerException("Product not found with Id: " + productId);
        }
    }
}
