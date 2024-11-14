package tdd.study.order_service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService      productService   ;
    @Autowired
    private ProductPort         productPort      ;
    @Autowired
    private ProductRepository   productRepository;



    @Test
    @DisplayName("상품등록 서비스")
    void registerProduct(){
        AddProductRequest request = 상품등록요청_생성();
        productService.addProduct(request);

        // API 요청
    }

    private static AddProductRequest 상품등록요청_생성(){
        final String productNm              = "상품명";
        final int price                     = 10_000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        return new AddProductRequest(productNm, price, discountPolicy);
    }

}
