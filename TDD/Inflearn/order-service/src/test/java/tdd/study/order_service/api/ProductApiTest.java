package tdd.study.order_service.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import tdd.study.order_service.ApiTest;
import tdd.study.order_service.product.AddProductRequest;
import tdd.study.order_service.product.DiscountPolicy;

class ProductApiTest extends ApiTest {


    @Test
    @DisplayName("상품등록 서비스")
    void registerApiProduct(){
        AddProductRequest request = 상품등록요청_생성_API();


        // ** API 요청
        final ExtractableResponse<Response> response =  RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/products")
                .then()
                .log().all().extract();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static AddProductRequest 상품등록요청_생성_API(){
        final String productNm              = "상품명";
        final int price                     = 10_000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        return new AddProductRequest(productNm, price, discountPolicy);
    }
}
