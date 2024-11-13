package tdd.study.order_service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    private ProductService      productService   ;
    private ProductPort         productPort      ;

    private ProductRepository   productRepository;

    @BeforeEach
    void setUp() {
        productRepository   = new ProductRepository();
        productPort         = new ProductAdapter(productRepository);
        productService      = new ProductService(productPort);
    }

    @Test
    @DisplayName("상품등록 서비스")
    void registerProduct(){
        final String productNm              = "상품명";
        final int price                     = 10_000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        final AddProductRequest request     = new AddProductRequest(productNm, price, discountPolicy);
        productService.addProduct(request);
    }

}
