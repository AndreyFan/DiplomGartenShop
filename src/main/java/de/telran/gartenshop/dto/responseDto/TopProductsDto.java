package de.telran.gartenshop.dto.responseDto;

import de.telran.gartenshop.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@AllArgsConstructor
public class TopProductsDto {
    private ProductEntity productEntity;
    private int quantity;
}
