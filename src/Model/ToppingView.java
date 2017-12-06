package Model;

import javafx.beans.property.SimpleStringProperty;

public class ToppingView {

    private int id;
    private int toppingTypeId;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;

    public ToppingView(int id, int toppingTypeId, String name, String type) {
        this.id = id;
        this.toppingTypeId = toppingTypeId;
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToppingTypeId() { return toppingTypeId; }

    public void setToppingTypeId(int toppingTypeId) { this.toppingTypeId = toppingTypeId; }

    public String getName() { return name.get(); }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
