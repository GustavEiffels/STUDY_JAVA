package chapter_03;

public class Sugar extends CoffeeDecorator{

    public Sugar(Coffee decoratedCoffee){
        super(decoratedCoffee);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.5;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Sugar";
    }
    
}
