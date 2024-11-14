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
