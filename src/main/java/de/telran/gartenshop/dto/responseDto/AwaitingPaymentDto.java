package de.telran.gartenshop.dto.responseDto;

import de.telran.gartenshop.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AwaitingPaymentDto {
    private ProductEntity product;
    private int totalQuantity;
}
