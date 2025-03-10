package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.OrderRequestDto;
import de.telran.gartenshop.dto.responsedto.OrderResponseDto;
import de.telran.gartenshop.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Tag(name = "Orders", description = "Controller for working with Orders")
@Validated
public interface OrderControllerInterface {

    @Operation(summary = "Find status of Orders", description = "Allows to view the order status for a user",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public ResponseEntity<OrderStatus> getOrderStatus(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long id);

    @Operation(summary = "All Orders", description = "Retrieves all orders",
              security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<OrderResponseDto> getAllOrders();

    @Operation(summary = "Create a new Order", description = "Order placement, search by userId, all items from " +
            "CartItems are moved to OrderItems",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public OrderResponseDto createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "User's purchase history", description = "Retrieves a user's purchase history",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public Set<OrderResponseDto> getUsersOrders(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "Cancel order by orderId", description = "Allows cancelling an order by its orderId",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    public OrderResponseDto cancelOrder(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long orderId);
}
