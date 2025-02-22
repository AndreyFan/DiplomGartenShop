package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.queryDto.ProductProfitDto;
import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.exception.BadRequestException;
import de.telran.gartenshop.exception.DataNotFoundInDataBaseException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CategoryRepository;
import de.telran.gartenshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    public List<ProductResponseDto> getProductsByFilter(Long categoryId, Double minPrice, Double maxPrice,
                                                        Boolean isDiscount, String sort) {
        if (minPrice == null) {
            minPrice = 0.00;
        }
        if (maxPrice == null) {
            maxPrice = Double.MAX_VALUE;
        }
        if (minPrice > maxPrice) {
            throw new BadRequestException("min_price must be <= max_price");
        }

        CategoryEntity categoryEntity = null;
        if (categoryId != null) {
            categoryEntity = categoryRepository.findById(categoryId).
                    orElseThrow(() -> new DataNotFoundInDataBaseException("Category not found with Id: " + categoryId));
        }

        List<ProductEntity> productEntity = productRepository.findProductByFilter(categoryEntity, minPrice, maxPrice,
                isDiscount, sort);
        List<ProductResponseDto> productResponseDtoList = MapperUtil.convertList(productEntity, mappers::convertToProductResponseDto);
        return productResponseDtoList;
    }

    public ProductResponseDto getProductById(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundInDataBaseException("Product not found with Id: " + productId));
        return mappers.convertToProductResponseDto(productEntity);
    }

    public boolean createProduct(ProductRequestDto productRequestDto) {
        ProductEntity createProductEntity = mappers.convertToProductEntity(productRequestDto);

        Long categoryId = productRequestDto.getCategoryId();

        CategoryEntity categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundInDataBaseException("Category not found with Id: " + categoryId));

        Timestamp timestamp = new Timestamp(new Date().getTime());

        createProductEntity.setProductId(null);
        createProductEntity.setCreatedAt(timestamp);
        createProductEntity.setUpdatedAt(timestamp);
        createProductEntity.setCategory(categoryEntity);
        ProductEntity savedProductEntity = productRepository.save(createProductEntity);
        return savedProductEntity.getProductId() != null;
    }

    public ProductResponseDto updateProduct(ProductRequestDto productRequestDto, Long productId) {
        ProductEntity updateProductEntity = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundInDataBaseException("Product not found with Id: " + productId));
        CategoryEntity categoryEntity = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new DataNotFoundInDataBaseException("Category not found with Id: " + productRequestDto.getCategoryId()));

        Timestamp timestamp = new Timestamp(new Date().getTime());

        updateProductEntity.setName(productRequestDto.getName());
        updateProductEntity.setDescription(productRequestDto.getDescription());
        updateProductEntity.setPrice(productRequestDto.getPrice());
        updateProductEntity.setCategory(categoryEntity);
        updateProductEntity.setImageUrl(productRequestDto.getImageUrl());
        updateProductEntity.setDiscountPrice(productRequestDto.getDiscountPrice());
        updateProductEntity.setUpdatedAt(timestamp);
        productRepository.save(updateProductEntity);
        return mappers.convertToProductResponseDto(updateProductEntity);
    }

    public ProductResponseDto updateDiscountPrice(ProductRequestDto productRequestDto, Long productId) {
        ProductEntity updateProductEntity = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundInDataBaseException("Product not found with Id: " + productId));

        Timestamp timestamp = new Timestamp(new Date().getTime());

        updateProductEntity.setDiscountPrice(productRequestDto.getDiscountPrice());
        updateProductEntity.setUpdatedAt(timestamp);
        productRepository.save(updateProductEntity);
        return mappers.convertToProductResponseDto(updateProductEntity);
    }

    public void deleteProduct(Long productId) {
        ProductEntity deleteProductEntity = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundInDataBaseException("Product not found with Id: " + productId));
        productRepository.delete(deleteProductEntity);
    }

    public ProductResponseDto getProductOfDay() {
        List<ProductEntity> productOfDayList = productRepository.getProductOfDay();
        if (productOfDayList.size() > 1) {
            Random random = new Random();
            int randomNum = random.nextInt(productOfDayList.size());
            return mappers.convertToProductResponseDto(productOfDayList.get(randomNum));
        } else {
            return mappers.convertToProductResponseDto(productOfDayList.getFirst());
        }
    }

    public List<ProductProfitDto> getProductProfitByPeriod(String period, Integer value) {
        return productRepository.getProductProfitByPeriod(period, value);
    }
}
