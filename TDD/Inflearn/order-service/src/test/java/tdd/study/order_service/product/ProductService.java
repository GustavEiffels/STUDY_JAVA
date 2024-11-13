package tdd.study.order_service.product;

class ProductService {

    final ProductPort productPort;

    ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }

    public void addProduct(final AddProductRequest request) {
        final Product product = new Product(request.productNm(), request.price(), request.discountPolicy());

        productPort.save(product);
    }
}
