package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/filter")
    //пример строки фильтра:    ?category=1&min_price=1&max_price=10&is_discount=true&sort=price,desc
    public List<ProductResponseDto> getProductsWithQuery(
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "min_price", required = false) Double minPrice,
            @RequestParam(value = "max_price", required = false) Double maxPrice,
            @RequestParam(value = "is_discount", required = false, defaultValue = "false") Boolean isDiscount,
            @RequestParam(value = "sort", required = false) String sort) {
        List<ProductResponseDto> productList = productService.getProductsByFilter(
                categoryId,
                minPrice,
                maxPrice,
                isDiscount,
                sort
        );
        return productList;
    }

    @GetMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @PutMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(@RequestBody ProductRequestDto productRequestDto, @PathVariable Long productId) {
        return productService.updateProduct(productRequestDto, productId);
    }

    @DeleteMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Long productId) { //delete
        productService.deleteProduct(productId);
    }

    //Товар дня (с max discountPrice)
    @GetMapping(value = "/productOfDay")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductOfDay() {
        return productService.getProductOfDay();
    }
}

