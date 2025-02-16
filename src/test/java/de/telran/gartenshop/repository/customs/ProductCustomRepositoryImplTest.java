package de.telran.gartenshop.repository.customs;

import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductCustomRepositoryImplTest {

    @Autowired
    private ProductRepository productRepositoryTest;

    private static CategoryEntity categoryEntityTest;

    @BeforeAll
    static void setStart() {
        categoryEntityTest = new CategoryEntity(1L, "CategoryName", null);
    }

    @Test
    public void getProductsByFilter1Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = true;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter2Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = true;
        String sort = "price,esc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter3Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = true;
        String sort = null;

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter4Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = true;
        String sort = "price";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter5Test() {
        Double minPrice = 9.99;
        Double maxPrice = null;
        Boolean isDiscount = true;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter6Test() {
        Double minPrice = null;
        Double maxPrice = 12.99;
        Boolean isDiscount = true;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter7Test() {
        Double minPrice = 12.99;
        Double maxPrice = 9.99;
        Boolean isDiscount = true;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter8Test() {
        Double minPrice = null;
        Double maxPrice = null;
        Boolean isDiscount = true;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter9Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = true;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(null, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter10Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = false;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }

    @Test
    public void getProductsByFilter11Test() {
        Double minPrice = 9.99;
        Double maxPrice = 12.99;
        Boolean isDiscount = null;
        String sort = "price,desc";

        List<ProductEntity> productEntityList = productRepositoryTest.findProductByFilter(categoryEntityTest, minPrice, maxPrice, isDiscount, sort);
        assertNotNull(productEntityList);
    }
}
