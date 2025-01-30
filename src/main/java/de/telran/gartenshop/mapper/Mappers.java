package de.telran.gartenshop.mapper;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class Mappers {
    private ModelMapper modelMapper;

    public Mappers(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryResponseDto convertToCategoryResponseDto(CategoryEntity categoryEntity) {
        return modelMapper.map(categoryEntity, CategoryResponseDto.class);
    }

    public CategoryEntity convertToCategoryEntity(CategoryRequestDto categoryRequestDto) {
        return modelMapper.map(categoryRequestDto, CategoryEntity.class);
    }

    public UserRequestDto convertToUserResponseDto(UserEntity user) {
        return modelMapper.map(user, UserRequestDto.class);
    }
}
