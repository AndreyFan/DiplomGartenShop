package de.telran.gartenshop.controller;


import de.telran.gartenshop.dto.querydto.ProductAwaitingPaymentDto;
import de.telran.gartenshop.dto.querydto.ProductProfitDto;
import de.telran.gartenshop.dto.querydto.ProductTopPaidCanceledDto;
import de.telran.gartenshop.dto.requestdto.ProductRequestDto;
import de.telran.gartenshop.dto.responsedto.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Products", description = "Controller for managing Products")
@Validated
public interface ProductControllerInterface {

    @Operation(summary = "Retrieve all products", description = "This endpoint retrieves all products in the catalog")
    public List<ProductResponseDto> getAllProducts();

    @Operation(
            summary = "Retrieve filtered products",
            description = "This endpoint allows you to filter products by various criteria such as category, price" +
                    " range, discount status, and sorting.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of filtered products successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid query parameters",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    public List<ProductResponseDto> getProductsByFilter(
            @Parameter(description = "The category ID to filter products by. Optional, default is all categories", example = "1")
            @RequestParam(value = "category", required = false)
            @Min(value = 1, message = "Invalid category Id: category Id must be >= 1") Long categoryId,

            @Parameter(description = "The minimum price to filter products by. Optional, default is no minimum price", example = "10.00")
            @RequestParam(value = "min_price", required = false)
            @DecimalMin(value = "0.00", message = "Invalid min_price: Must be > 0.")
            @Digits(integer = 7, fraction = 2, message = "Invalid min_price: Must be a number with up to 7 digits" +
                    " before and 2 after the decimal.") Double minPrice,

            @Parameter(description = "The maximum price to filter products by. Optional, default is no maximum " +
                    "price", example = "20.00")
            @RequestParam(value = "max_price", required = false)
            @DecimalMin(value = "0.00", message = "Invalid max_price: Must be > 0.")
            @Digits(integer = 7, fraction = 2, message = "Invalid min_price: Must be a number with up to 7 digits " +
                    "before and 2 after the decimal.") Double maxPrice,

            @Parameter(description = "Filter products by discount status. Optional, default is true.",  example = "true" )
            @RequestParam(value = "is_discount", required = false, defaultValue = "true")
            @NotNull(message = "is_discount can not be null, must be true/false.") Boolean isDiscount,

            @Parameter(description = "Sort the products by a specific field (name, price, discountPrice, createdAt," +
                    " updatedAt), followed by the sorting order (asc or desc). Optional, default is no sorting",
                    example = "price,asc")
            @RequestParam(value = "sort", required = false)
            @Pattern(regexp = "^(name|price|discountPrice|createdAt|updatedAt)(,(asc|desc))?$",
                    message = "Invalid parameter definition: parameter must be = name|price|discountPrice|createdAt|updatedAt" +
                            "Invalid sorting definition: must be in form '<sort parameter>,<sort order>'") String sort);


    @Operation(summary = "Retrieve product by ID", description = "This endpoint retrieves a product from the catalog" +
            " using the specified product ID")
    public ProductResponseDto getProductById(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId);

    @Operation(summary = "Create a new product", description = "This endpoint allows you to create a new product " +
            "in the catalog by providing the product details",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public boolean createProduct(
            @Parameter(description = "The product details to be created", required = true)
            @RequestBody @Valid ProductRequestDto productRequestDto);

    @Operation(summary = "Update product by ID", description = "This endpoint allows you to update the details" +
            " of an existing product using the specified product ID",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ProductResponseDto updateProduct(
            @Parameter(description = "The updated product details", required = true)
            @RequestBody @Valid ProductRequestDto productRequestDto,
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId);

    @Operation(summary = "Apply discount to a product", description = "This endpoint allows an administrator" +
            " to apply a discount to a specific product identified by the product ID." +
            " The discount price is set through the request body",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ProductResponseDto updateDiscountPrice(
            @Parameter(description = "The updated product discount details", required = true)
            @RequestBody @Valid ProductRequestDto productRequestDto,
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId);

    @Operation(summary = "Delete a product by ID", description = "This endpoint allows you to delete a " +
            "product from the catalog using the specified product ID",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public void deleteProduct(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long productId);

    @Operation(summary = "Get the product of the day", description = "This endpoint retrieves the product with " +
            "the highest discount, marked as the product of the day")
    public ProductResponseDto getProductOfDay();

    @Operation(summary = "Get product profit by period", description = "This endpoint calculates the product profit " +
            "grouped by specified time periods (e.g., days, week, months) and a value to define the range",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<ProductProfitDto> getProfitByPeriod(
            @Parameter(description = "The period for grouping the profit (e.g., 'day', 'week', 'month')",
                    required = true, example = "MONTH")
            @RequestParam("period")
            @Pattern(regexp = "^(?i)(DAY|WEEK|MONTH)$", message = "Invalid type of period: Must be DAY, WEEK or MONTH " +
                    "(case insensitive)") String period,
            @Parameter(description = "The value to define the range for the specified period (e.g., the number " +
                    "of days, months, or years)", required = true, example = "3")
            @RequestParam("value")
            @Positive(message = "Period length must be a positive number") Integer value);

    @Operation(summary = "Get top ten products", description = "Allows to retrieve the top 10 most purchased products",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<ProductTopPaidCanceledDto> getTop10PaidProducts();

    @Operation(summary = "Get top ten canceled products", description = "Allows to retrieve top 10 most" +
            " frequently canceled products",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<ProductTopPaidCanceledDto> getTop10CanceledProducts();

    @Operation(summary = "Get products in the 'Awaiting payment' status ", description = "Retrieves a list of products that have been in the" +
            " 'Awaiting payment' status for more than N days",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<ProductAwaitingPaymentDto> getAwaitingPaymentProducts(
            @RequestParam(name = "days", defaultValue = "10")
            @Positive(message = "Number of days must be > 0") int days);
}