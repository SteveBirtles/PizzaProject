package Model;

import javafx.beans.property.SimpleStringProperty;

public class ToppingView {

    private int id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;

    public ToppingView(int id, String name, String type) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

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
