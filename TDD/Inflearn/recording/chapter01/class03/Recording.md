### API 요청 TEST 로 변환 
- 요청 TEST 로 변환하기 위해 의존성 추가
// https://mvnrepository.com/artifact/io.rest-assured/rest-assured

```build

	testImplementation 'io.rest-assured:rest-assured:5.4.0'


```

### API TEST 
```java
package tdd.study.order_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }
}
```


```java
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

```

```java
package tdd.study.order_service.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductService {

    final ProductPort productPort;

    public ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }


    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody  final AddProductRequest request) {
        final Product product = new Product(request.productNm(), request.price(), request.discountPolicy());

        productPort.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

```