package Model;

public class PizzaTopping {

    private int pizzaId;
    private int toppingId;

    public PizzaTopping(int pizzaId, int toppingId) {
        this.pizzaId = pizzaId;
        this.toppingId = toppingId;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    @Override
    public String toString() {
        return  "pizzaId=" + pizzaId +
                ", toppingId=" + toppingId;
    }

}
