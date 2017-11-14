package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PizzaService {

    public static void selectAll(List<Pizza> targetList, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("SELECT id, name FROM Pizzas");

        try {
            if (statement != null) {

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Pizza(
                                results.getInt("id"),
                                results.getString("name")
                        ));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static Pizza selectById(int id, DatabaseConnection database) {

        Pizza result = null;

        PreparedStatement statement = database.newStatement("SELECT id, name FROM Pizzas WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new Pizza(
                            results.getInt("id"),
                            results.getString("name"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }

        return result;
    }

    public static void selectPizzaToppings(Pizza selectedItem, List<PizzaTopping> targetList, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("SELECT pizzaId, toppingId FROM PizzaToppings WHERE pizzaId = ?");

        try {
            if (statement != null) {
                statement.setInt(1, selectedItem.getId());

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add(new PizzaTopping(
                                results.getInt("pizzaId"),
                                results.getInt("toppingId")
                        ));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static void save(Pizza itemToSave, DatabaseConnection database) {

        Pizza existingItem = null;
        if (itemToSave.getId() != 0) existingItem = selectById(itemToSave.getId(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO Pizzas (name) VALUES (?)");
                statement.setString(1, itemToSave.getName());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE Pizzas SET name = ? WHERE id = ?");
                statement.setString(1, itemToSave.getName());
                statement.setInt(2, itemToSave.getId());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }

    public static void savePizzaTopping(PizzaTopping itemToSave, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("INSERT INTO PizzaToppings (pizzaId, toppingId) VALUES (?, ?)");
        try {
            statement.setInt(1, itemToSave.getPizzaId());
            statement.setInt(2, itemToSave.getToppingId());
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }

    }

    public static void deletePizzaTopping(int pizzaId, int toppingId, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM PizzaToppings WHERE pizzaId = ? AND toppingId = ?");
        try {
            statement.setInt(1, pizzaId);
            statement.setInt(2, toppingId);
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }

    }

    @SuppressWarnings("Duplicates")
    public static void deleteById(int id, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM Pizzas WHERE id = ?");

        try {
            statement.setInt(1, id);
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }

    @SuppressWarnings("Duplicates")
    public static void deletePizzaToppingsByPizzaId(int id, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM PizzaToppings WHERE pizzaId = ?");

        try {
            statement.setInt(1, id);
            database.executeUpdate(statement);
        }
        catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }

}
