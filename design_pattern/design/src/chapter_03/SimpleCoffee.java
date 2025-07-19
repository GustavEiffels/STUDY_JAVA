package chapter_03;

public class SimpleCoffee implements Coffee{

    @Override
    public double getCost() {
        return 5.0; // 기본 커피 비용
    }

    @Override
    public String getDescription() {
        return "Simple Coffee"; // 기본 커피 설명
    }
    
}
