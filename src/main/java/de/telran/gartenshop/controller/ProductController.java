package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.querydto.ProductAwaitingPaymentDto;
import de.telran.gartenshop.dto.querydto.ProductProfitDto;
import de.telran.gartenshop.dto.querydto.ProductTopPaidCanceledDto;
import de.telran.gartenshop.dto.requestdto.ProductRequestDto;
import de.telran.gartenshop.dto.responsedto.ProductResponseDto;
import de.telran.gartenshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products")
public class ProductController implements ProductControllerInterface {
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
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "min_price", required = false) Double minPrice,
            @RequestParam(value = "max_price", required = false) Double maxPrice,
            @RequestParam(value = "is_discount", required = false, defaultValue = "false") Boolean isDiscount,
            @RequestParam(value = "sort", required = false) String sort) {

        return productService.getProductsByFilter(categoryId, minPrice, maxPrice, isDiscount, sort);
    }

    //Просмотр товара по Id //localhost:8088/products/1
    @GetMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(
            @PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    //Добавление нового товара //localhost:8088/products
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public boolean createProduct(
            @RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    //Редактирование товара по Id //localhost:8088/products/1
    @PutMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ProductResponseDto updateProduct(
            @RequestBody ProductRequestDto productRequestDto,
            @PathVariable Long productId) {
        return productService.updateProduct(productRequestDto, productId);
    }

    //Добавить скидку на товар по productId //localhost:8088/products/discount/1
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PutMapping(value = "/discount/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateDiscountPrice(
            @RequestBody ProductRequestDto productRequestDto,
            @PathVariable Long productId) {
        return productService.updateDiscountPrice(productRequestDto, productId);
    }

    //Удаление товара по Id //localhost:8088/products/1
    @DeleteMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void deleteProduct(
            @PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

    //Товар дня (с наибольшей скидкой) //localhost:8088/products/productOfDay
    @GetMapping(value = "/productOfDay")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductOfDay() {
        return productService.getProductOfDay();
    }

    //Прибыль с группировкой по периодам
    //localhost:8088/products/profit?period=DAY&value=60
    @GetMapping(value = "/profit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public List<ProductProfitDto> getProfitByPeriod(
            @RequestParam("period") String period,
            @RequestParam("value") Integer value) {
        return productService.getProductProfitByPeriod(period, value);
    }

    //Топ 10 купленных товаров
    //localhost:8088/products/top10paid
    @GetMapping(value = "/top10paid")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public List<ProductTopPaidCanceledDto> getTop10PaidProducts() {
        return productService.getTop10PaidProducts();
    }

    //Топ 10 часто отменяемых товаров
    //localhost:8088/products/top10canceled
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/top10canceled")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductTopPaidCanceledDto> getTop10CanceledProducts() {
        return productService.getTop10CanceledProducts();
    }

    //Список товаров, которые находятся в статусе Ожидает оплаты более N дней
    //localhost:8088/products/awaiting-payment-products?days=10
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/awaiting-payment-products")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductAwaitingPaymentDto> getAwaitingPaymentProducts(@RequestParam(name = "days",
            defaultValue = "10") int days) {
        return productService.getAwaitingPaymentProducts(days);
    }
}

