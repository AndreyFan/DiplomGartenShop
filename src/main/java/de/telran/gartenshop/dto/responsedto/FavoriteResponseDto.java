package de.telran.gartenshop.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponseDto {
    private Long favoriteId;
    private UserResponseDto userResponseDto;
    private ProductResponseDto productResponseDto;
}
