package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ToppingService {

    public static void selectAll(List<Topping> targetList, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("SELECT id, name FROM Toppings ORDER BY name");

        try {
            if (statement != null) {

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Topping(
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

    public static Topping selectById(int id, DatabaseConnection database) {

        Topping result = null;

        PreparedStatement statement = database.newStatement("SELECT id, name FROM Toppings WHERE id = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new Topping(
                            results.getInt("id"),
                            results.getString("name"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }

        return result;
    }

    public static void save(Topping itemToSave, DatabaseConnection database) {

        Topping existingItem = null;
        if (itemToSave.getId() != 0) existingItem = selectById(itemToSave.getId(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO Toppings (name) VALUES (?)");
                statement.setString(1, itemToSave.getName());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE Toppings SET name = ? WHERE id = ?");
                statement.setString(1, itemToSave.getName());
                statement.setInt(2, itemToSave.getId());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }

    }

    @SuppressWarnings("Duplicates")
    public static void deleteById(int id, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM Toppings WHERE id = ?");

        try {
            if (statement != null) {
                statement.setInt(1, id);
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }

    @SuppressWarnings("Duplicates")
    public static void deletePizzaToppingsByToppingId(int id, DatabaseConnection database) {

        PreparedStatement statement = database.newStatement("DELETE FROM PizzaToppings WHERE toppingId = ?");

        try {
            if (statement != null) {
                statement.setInt(1, id);
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }

}
