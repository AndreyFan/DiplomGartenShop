package de.telran.gartenshop.repository.customs;

import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;

import java.util.List;

public interface ProductCustomRepository {
    List<ProductEntity> findProductByFilter(CategoryEntity categoryEntity, Double minPrice, Double maxPrice,
                                            Boolean isDiscount, String sort); // price DESC, name
}
