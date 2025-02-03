package de.telran.gartenshop.repository.customs;

import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductEntity> findProductByFilter(CategoryEntity category, Double minPrice, Double maxPrice, Boolean isDiscount, String sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ProductEntity> query = cb.createQuery(ProductEntity.class); //SELECT
        Root<ProductEntity> root = query.from(ProductEntity.class); //FROM

        //WHERE
        List<Predicate> predicates = new ArrayList<>();
        if (category != null) {
            predicates.add(cb.equal(root.get("category"), category)); // categoryId = 1
        }
        if (isDiscount != null && isDiscount) {
            predicates.add(cb.gt(root.get("discountPrice"), 0)); // discountPrice > 0
        }

        Predicate filterPrice = null;
        if (minPrice != null && maxPrice != null && minPrice < maxPrice) {
            filterPrice = cb.between(root.get("price"), minPrice, maxPrice); // price BETWEEN (1,100)
        } else if (minPrice != null && maxPrice == null) { //>minPrice
            filterPrice = cb.gt(root.get("price"), minPrice); // price > 1
        } else if (maxPrice != null && minPrice == null) { //<maxPrice
            filterPrice = cb.lt(root.get("price"), maxPrice); // price < 10
        }

        if (filterPrice != null) {
            predicates.add(filterPrice); // добавляем в предикат условие по цене
        }

        // ORDER BY (SORT) ("price", "name,DESC")
        Order sortOrder = null;
        if (sort != null) {
            String[] sortArr = sort.split(",");
            if (sortArr.length == 2) {
                if (sortArr[1].equalsIgnoreCase("DESC")) {
                    sortOrder = cb.desc(root.get(sortArr[0]));
                } else {
                    sortOrder = cb.asc(root.get(sortArr[0]));
                }
            } else { //если не передали тип сортировки
                sortOrder = cb.asc(root.get(sortArr[0]));
            }
        } else { //сортировка по умолчанию
            sortOrder = cb.asc(root.get("name"));
        }

        if (sortOrder == null) { //сортировка по умолчанию
            sortOrder = cb.asc(root.get("name"));
        }
        // Нужно сделать ExceptionHandler на org.springframework.dao.InvalidDataAccessApiUsageException,
        // или org.hibernate.query.sqm.PathElementException, если не правильно передали поле для сортировки

        query.select(root)
                .where(cb.and(predicates.toArray(new Predicate[predicates.size()])))
                .orderBy(sortOrder);

        return entityManager.createQuery(query).getResultList();
    }
}
