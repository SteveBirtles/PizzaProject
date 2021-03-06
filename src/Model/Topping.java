package Model;

public class Topping {

    private int id;
    private String name;
    private int toppingTypeId;

    public Topping(int id, String name, int toppingTypeId) {
        this.id = id;
        this.name = name;
        this.toppingTypeId = toppingTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToppingTypeId() {
        return toppingTypeId;
    }

    public void setToppingTypeId(int toppingTypeId) {
        this.toppingTypeId = toppingTypeId;
    }

    @Override
    public String toString() {
        return name;
    }

}
