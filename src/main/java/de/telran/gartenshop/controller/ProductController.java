package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.queryDto.ProductProfitDto;
import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products")
@Validated
public class ProductController {
    private final ProductService productService;

    //Просмотр всех товаров каталога //localhost:8088/products/1
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    //Просмотр и фильтрация товара
    //localhost:8088/products/filter?category=3&min_price=10&max_price=90&is_discount=true&sort=price,desc
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/filter")
    public List<ProductResponseDto> getProductsByFilter(
            @RequestParam(value = "category", required = false)
            @Min(value = 1, message = "Invalid category Id: category Id must be >= 1") Long categoryId,

            @RequestParam(value = "min_price", required = false)
            @DecimalMin(value = "0.00", message = "Invalid min_price: Must be > 0.")
            @Digits(integer = 7, fraction = 2, message = "Invalid min_price: Must be a number with up to 7 digits before and 2 after the decimal.") Double minPrice,

            @RequestParam(value = "max_price", required = false)
            @DecimalMin(value = "0.00", message = "Invalid max_price: Must be > 0.")
            @Digits(integer = 7, fraction = 2, message = "Invalid min_price: Must be a number with up to 7 digits before and 2 after the decimal.") Double maxPrice,

            @RequestParam(value = "is_discount", required = false, defaultValue = "false")
            @NotNull(message = "is_discount can not be null, must be true/false.") Boolean isDiscount,

            @RequestParam(value = "sort", required = false)
            @Pattern(regexp = "^(name|price|discountPrice|createdAt|updatedAt)(,(asc|desc))?$",
                    message = "Invalid parameter definition: parameter must be = name|price|discountPrice|createdAt|updatedAt. " +
                            "Invalid sorting definition: must be in form '<sort parameter>,<sort order>'") String sort) {
        List<ProductResponseDto> productList = productService.getProductsByFilter(
                categoryId,
                minPrice,
                maxPrice,
                isDiscount,
                sort
        );
        return productList;
    }

    //Просмотр товара по Id //localhost:8088/products/1
    @GetMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId) {
        return productService.getProductById(productId);
    }

    //Добавление нового товара //localhost:8088/products
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    //Редактирование товара по Id //localhost:8088/products/1
    @PutMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(
            @RequestBody @Valid ProductRequestDto productRequestDto,
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId) {
        return productService.updateProduct(productRequestDto, productId);
    }

    //Добавить скидку на товар по productId //localhost:8088/products/discount/1
    @PutMapping(value = "/discount/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateDiscountPrice(
            @RequestBody @Valid ProductRequestDto productRequestDto,
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId) {
        return productService.updateDiscountPrice(productRequestDto, productId);
    }

    //Удаление товара по Id //localhost:8088/products/1
    @DeleteMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId) {
        productService.deleteProduct(productId);
    }

    //Товар дня (с наибольшей скидкой) //localhost:8088/products/productOfDay
    @GetMapping(value = "/productOfDay")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductOfDay() {
        return productService.getProductOfDay();
    }

    //Прибыль с группировкой по периодам
    @GetMapping(value = "/profit")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductProfitDto> getProfitByPeriod(
            @RequestParam("period")
            @Pattern(regexp = "^(?i)(WEEK|DAY|MONTH)$", message = "Invalid type of period: Must be DAY, WEEK or MONTH (case insensitive)") String period,

            @RequestParam("value")
            @Positive(message = "Period length must be a positive number") Integer value) {
        return productService.getProductProfitByPeriod(period, value);
    }
}

