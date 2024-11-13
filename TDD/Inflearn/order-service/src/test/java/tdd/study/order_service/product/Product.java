package tdd.study.order_service.product;

import org.springframework.util.Assert;

class Product {
    public Long getId() {
        return id;
    }

    private Long id;
    final String productNm;
    final int price;
    final DiscountPolicy discountPolicy;

    public Product(final String productNm, final int price, final DiscountPolicy discountPolicy) {
        Assert.hasText(productNm, "상품명은 필수입니다.");
        Assert.isTrue(price > 0, "상품 가격은 0 보다 커야합니다.");
        Assert.notNull(discountPolicy, "할인 적용 상태는 필수 입니다.");
        this.productNm = productNm;
        this.price = price;
        this.discountPolicy = discountPolicy;
    }

    public void assignId(final Long id) {
        this.id = id;
    }
}
