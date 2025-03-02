package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.CartItemRequestDto;
import de.telran.gartenshop.dto.responsedto.CartItemResponseDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Tag(name = "Carts", description = "Controller for adding CartItems",
        externalDocs = @ExternalDocumentation(description = "Link for external " +
                "documentation in German language", url = "https://gartenshopExDoc.de"
        )
)
@Validated
public interface CartControllerInterface {
    @Operation(summary = "All cartItems", description = "Get all the cartItems")
    public List<CartItemResponseDto> getAllCartItems();

    @Operation(summary = "Find CartItems by UserId", description = "Retrieves all cartItems for a user based on their userId. " +
            "This operation returns a set of cartItems")
    public Set<CartItemResponseDto> getAllCartItemsByUserId(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "Create a new CartItem", description = "Creates a new cartItem for the specified user by adding " +
            "the provided product details to their cart")
    public boolean createCartItem(@RequestBody @Valid CartItemRequestDto cartItemRequestDto,
                                  @Parameter(description = "Identifier", required = true, example = "1")
                                  @PathVariable
                                  @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);

    @Operation(summary = "Delete a CartItem", description = "Removes the specified cartItem from the user's cart based on the provided cartItemId")
    public void deleteCartItem(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long cartItemId);

    @Operation(summary = "Delete all CartItems", description = "Removes all items from the user's cart based on the provided userId")
    public void deleteAllCartItems(
            @Parameter(description = "Identifier", required = true, example = "1")
            @PathVariable
            @Min(value = 1, message = "Invalid Id: Id must be >= 1") Long userId);
}

