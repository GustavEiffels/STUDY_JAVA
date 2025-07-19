package chapter_03;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("--- 커피 주문 데모 ---");

        // 1. 일반 커피 주문
        Coffee myCoffee = new SimpleCoffee();
        System.out.printf("주문 1: %s - $%.2f%n", myCoffee.getDescription(), myCoffee.getCost());
        // 출력: 주문 1: Simple Coffee - $5.00

        // 2. 우유 추가 커피 주문
        Coffee milkCoffee = new Milk(new SimpleCoffee());
        System.out.printf("주문 2: %s - $%.2f%n", milkCoffee.getDescription(), milkCoffee.getCost());
        // 출력: 주문 2: Simple Coffee, Milk - $6.50

        // 3. 우유와 설탕 추가 커피 주문
        Coffee milkSugarCoffee = new Sugar(new Milk(new SimpleCoffee()));
        System.out.printf("주문 3: %s - $%.2f%n", milkSugarCoffee.getDescription(), milkSugarCoffee.getCost());
        // 출력: 주문 3: Simple Coffee, Milk, Sugar - $7.00

        // 4. 설탕만 추가 커피 주문
        Coffee sugarCoffee = new Sugar(new SimpleCoffee());
        System.out.printf("주문 4: %s - $%.2f%n", sugarCoffee.getDescription(), sugarCoffee.getCost());
        // 출력: 주문 4: Simple Coffee, Sugar - $5.50

        System.out.println("--- 데모 종료 ---");
    }
}
