package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.FavoriteEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.exception.UserNotFoundException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.FavoriteRepository;
import de.telran.gartenshop.repository.ProductRepository;
import de.telran.gartenshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private FavoriteRepository favoriteRepositoryMock;

    @InjectMocks
    private FavoriteService favoriteServiceMock;

    @Mock
    private Mappers mappersMock;

    private UserEntity userEntityTest;
    private ProductEntity productEntityTest;
    private FavoriteEntity favoriteEntityTest;

    private UserResponseDto userResponseDtoTest;
    private ProductResponseDto productResponseDtoTest;
    private FavoriteResponseDto favoriteResponseDtoTest;

    private FavoriteRequestDto favoriteRequestDtoTest;

    private Set<FavoriteEntity> favoriteEntitySetTest = new HashSet<>();
    private Set<FavoriteResponseDto> favoriteResponseDtoSetTest = new HashSet<>();

    UserNotFoundException userNotFoundException;

    Timestamp timestamp;
    Long userIdTest = 1L;
    Long userIdTestFalse = 2L;

    @BeforeEach
    void setUp() {
        timestamp = new Timestamp(new Date().getTime());

        userEntityTest = new UserEntity(
                1L,
                "Tom Smith",
                "ts@gmail.com",
                "+4975644333",
                "hgfgjfdlgjflg",
                Role.CLIENT,
                null,
                null,
                null);


        CategoryEntity categoryEntityTest = new CategoryEntity(1L, "CategoryName", null);
        productEntityTest = new ProductEntity(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                categoryEntityTest);

        favoriteEntityTest = new FavoriteEntity(
                1L,
                userEntityTest,
                productEntityTest);

        favoriteEntitySetTest.add(favoriteEntityTest);
        userEntityTest.setFavorites(favoriteEntitySetTest);

        UserResponseDto userResponseDtoTest = new UserResponseDto(
                1L,
                "Tom Smith",
                "ts@gmail.com",
                "+4975644333",
                "hgfgjfdlgjflg",
                Role.CLIENT);

        ProductResponseDto productResponseDtoTest = new ProductResponseDto(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                new CategoryResponseDto(1L, "CategoryName"));

        favoriteResponseDtoTest = new FavoriteResponseDto(
                1L,
                userResponseDtoTest,
                productResponseDtoTest);

        favoriteResponseDtoSetTest.add(favoriteResponseDtoTest);

        favoriteRequestDtoTest = new FavoriteRequestDto(
                1L,
                1L);
    }

    @Test
    void getFavoritesByUserIdTest() {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        when(mappersMock.convertToFavoriteResponseDto(favoriteEntityTest)).thenReturn(favoriteResponseDtoTest);

        Set<FavoriteResponseDto> actualFavoriteResponseDtoSetTest = favoriteServiceMock.getFavoritesByUserId(userIdTest);

        //проверка
        assertNotNull(actualFavoriteResponseDtoSetTest);
        assertEquals(1, favoriteResponseDtoSetTest.size());
        assertEquals(favoriteResponseDtoSetTest.size(), actualFavoriteResponseDtoSetTest.size());
        assertEquals(favoriteResponseDtoSetTest.hashCode(), actualFavoriteResponseDtoSetTest.hashCode());

        verify(userRepositoryMock, times(1)).findById(userIdTest); // был ли запущен этот метод и ск. раз
        verify(mappersMock, times(1)).convertToFavoriteResponseDto(any(FavoriteEntity.class));

        when(userRepositoryMock.findById(userIdTestFalse)).thenReturn(Optional.empty());
        userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> favoriteServiceMock.getFavoritesByUserId(userIdTestFalse));
        assertEquals(" This user not found ", userNotFoundException.getMessage());
    }

    @Test
    void getFavoritesTest() {
        String emailTest = "ts@gmail.com";
        String emailTestFalse = "ts555@gmail.com";

        when(userRepositoryMock.findByEmail(emailTest)).thenReturn(userEntityTest);
        when(mappersMock.convertToFavoriteResponseDto(favoriteEntityTest)).thenReturn(favoriteResponseDtoTest);

        Set<FavoriteResponseDto> actualFavoriteResponseDtoSetTest = favoriteServiceMock.getFavorites(emailTest);

        //проверка
        assertNotNull(actualFavoriteResponseDtoSetTest);
        assertEquals(1, favoriteResponseDtoSetTest.size());
        assertEquals(favoriteResponseDtoSetTest.size(), actualFavoriteResponseDtoSetTest.size());
        assertEquals(favoriteResponseDtoSetTest.hashCode(), actualFavoriteResponseDtoSetTest.hashCode());

        verify(userRepositoryMock, times(1)).findByEmail(emailTest); // был ли запущен этот метод и ск. раз
        verify(mappersMock, times(1)).convertToFavoriteResponseDto(any(FavoriteEntity.class));

        when(userRepositoryMock.findByEmail(emailTestFalse)).thenReturn(null);
        userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> favoriteServiceMock.getFavorites(emailTestFalse));
        assertEquals(" This user not found ", userNotFoundException.getMessage());
    }

//    @Test
//    void createFavoriteTest() {
//        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
//        when(productRepositoryMock.findById(favoriteRequestDtoTest.getProductId())).thenReturn(Optional.of(productEntityTest));
//
//        favoriteServiceMock.createFavorite(favoriteRequestDtoTest);
//
//        verify(favoriteRepositoryMock, times(1)).save(any(FavoriteEntity.class));

//        when(userRepositoryMock.findByEmail(wrongEmail)).thenReturn(Optional.empty());
//        dataNotFoundInDataBaseException = assertThrows(DataNotFoundInDataBaseException.class,
//                () -> favoriteServiceMock.insertFavorite(favoriteRequestDto, wrongEmail));
//        assertEquals("User not found in database.", dataNotFoundInDataBaseException.getMessage());
//
//        when(productRepositoryMock.findById(wrongFavoriteRequestDto.getProductId())).thenReturn(Optional.empty());
//        dataNotFoundInDataBaseException = assertThrows(DataNotFoundInDataBaseException.class,
//                () -> favoriteServiceMock.insertFavorite(wrongFavoriteRequestDto, email));
//        assertEquals("Product not found in database.", dataNotFoundInDataBaseException.getMessage());
//
//        when(productRepositoryMock.findById(existingFavoriteRequestDto.getProductId())).thenReturn(Optional.of(product));
//        dataAlreadyExistsException = assertThrows(DataAlreadyExistsException.class,
//                () -> favoriteServiceMock.insertFavorite(existingFavoriteRequestDto, email));
//        assertEquals("This product is already in favorites.", dataAlreadyExistsException.getMessage());
  //  }

}