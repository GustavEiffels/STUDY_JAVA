package chapter_03;

public class Milk extends CoffeeDecorator {
    public Milk(Coffee decoratedCoffee) {
        super(decoratedCoffee); // 부모 클래스(CoffeeDecorator)의 생성자 호출
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.5; // 기존 비용에 우유 비용 추가
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Milk"; // 기존 설명에 우유 설명 추가
    }
}