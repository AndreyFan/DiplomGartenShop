package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestdto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responsedto.FavoriteResponseDto;
import de.telran.gartenshop.entity.FavoriteEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.exception.DataNotFoundInDataBaseException;
import de.telran.gartenshop.exception.UserNotFoundException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.FavoriteRepository;
import de.telran.gartenshop.repository.ProductRepository;
import de.telran.gartenshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;
    private final Mappers mappers;

    String userNotFoundException = "User not found with Id: ";

    public Set<FavoriteResponseDto> getFavoritesByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UserNotFoundException(userNotFoundException + userId);
        } else {
            Set<FavoriteEntity> favorites = user.getFavorites();
            return MapperUtil.convertSet(favorites, mappers::convertToFavoriteResponseDto);
        }
    }

    public Set<FavoriteResponseDto> getFavorites(String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        } else {
            Set<FavoriteEntity> favorites = user.getFavorites();
            return MapperUtil.convertSet(favorites, mappers::convertToFavoriteResponseDto);
        }
    }

    public Boolean createFavorite(FavoriteRequestDto favoriteRequestDto) {

        UserEntity user = userRepository.findById(favoriteRequestDto.getUserId()).orElse(null);
        if (user != null) {
            ProductEntity product = productRepository.findById(favoriteRequestDto.getProductId()).orElse(null);
            if (product != null) {
                Set<FavoriteEntity> favoriteEntitySet = user.getFavorites();
                for (FavoriteEntity f : favoriteEntitySet) {
                    if (f.getProduct().getProductId().equals(favoriteRequestDto.getProductId())) {
                        return true;
                        // if a specific user already has this product in their favorites -
                        // just return true without generating errors
                    }
                }

                FavoriteEntity favorite = new FavoriteEntity();
                favorite.setProduct(product);
                favorite.setUser(user);
                favoriteRepository.save(favorite);
                favoriteEntitySet.add(favorite);
                return favorite != null;
            } else {
                throw new DataNotFoundInDataBaseException("Product not found with Id: "+ favoriteRequestDto.getProductId());
            }
        } else {
            throw new UserNotFoundException(userNotFoundException + favoriteRequestDto.getUserId());
        }
    }

    public Boolean deleteFavorite(FavoriteRequestDto favoriteRequestDto) {

        UserEntity user = userRepository.findById(favoriteRequestDto.getUserId()).orElse(null);
        if (user != null) {
            Set<FavoriteEntity> favoriteEntitySet = user.getFavorites();
            for (FavoriteEntity f : favoriteEntitySet) {
                if (f.getProduct().getProductId().equals(favoriteRequestDto.getProductId())) {
                    favoriteEntitySet.remove(f);
                    favoriteRepository.deleteById(f.getFavoriteId());
                    return true;
                }
            }
            throw new DataNotFoundInDataBaseException("Product not found with Id: "+ favoriteRequestDto.getProductId());
        } else {
            throw new UserNotFoundException(userNotFoundException + favoriteRequestDto.getUserId());
        }
    }
}