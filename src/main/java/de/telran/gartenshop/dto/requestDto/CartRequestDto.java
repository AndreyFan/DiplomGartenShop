package de.telran.gartenshop.dto.requestDto;

public class CartRequestDto {
    private Long cartId;
    private Long userId;

    public CartRequestDto() {
    }

    public CartRequestDto(Long cartId, Long userId) {
        this.cartId = cartId;
        this.userId = userId;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
