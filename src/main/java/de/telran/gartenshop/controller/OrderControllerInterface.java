package de.telran.gartenshop.controller;


import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.OrderItemResponseDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.enums.OrderStatus;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Tag(name = "Orders-Endpoint", description = "Controller for working with Orders",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
public interface OrderControllerInterface {

    @Operation(summary = "Find status of Orders", description = "Allows to view the order status for a user")
    public ResponseEntity<OrderStatus> getOrderStatus(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable Long id);

    @Operation(summary = "Get top ten products", description = "Allows to retrieve the top 10 most purchased products")
    public ResponseEntity<List<ProductResponseDto>> getTop10PaidProducts();

    @Operation(summary = "Get top ten canceled products", description = "Allows to retrieve top 10 most" +
            " frequently canceled products")
    public ResponseEntity<List<ProductResponseDto>> getTopCanceled();

    @Operation(summary = "Get products in the 'Awaiting payment' status ", description = "Retrieves a list of products that have been in the" +
            " 'Awaiting payment' status for more than N days")
    public ResponseEntity<List<ProductResponseDto>> getAwaitingPaymentProducts(@RequestParam(name = "days",
            defaultValue = "10") int days);

    @Operation(summary = "All Orders", description = "Retrieves all orders")
    public List<OrderResponseDto> getAllOrders();

    @Operation(summary = "All OrderItems", description = "Retrieves all orderItems")
    public List<OrderItemResponseDto> getAllOrderItems();

    @Operation(summary = "Create a new Order", description = "Order placement, search by userId, all items from " +
            "CartItems are moved to OrderItems")
    public OrderResponseDto createOrder(
            @Parameter(description = "Identifier", required = true, example = "1")
            @RequestBody OrderRequestDto orderRequestDto, @PathVariable Long userId);

    @Operation(summary = "User's purchase history", description = "Retrieves a user's purchase history")
    public Set<OrderResponseDto> getUsersOrders(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable Long userId);

    @Operation(summary = "Cancel order by orderId", description = "Allows cancelling an order by its orderId")
    public OrderResponseDto cancelOrder(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable Long orderId);

}
