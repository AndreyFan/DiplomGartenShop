package de.telran.gartenshop.dto.requestdto;

import de.telran.gartenshop.entity.enums.DeliveryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    @Schema(description = "Delivery Address", example = "35 Pine Street")
    @NotNull(message = "Delivery address cannot be empty!")
    @Size(min = 1, max = 150, message = "Delivery address must be 1-150 characters")
    private String deliveryAddress;

    @NotNull(message = "Delivery method cannot be empty!")
    private DeliveryMethod deliveryMethod;
}
