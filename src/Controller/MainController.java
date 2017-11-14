package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Optional;

public class MainController {

    /* Here are references to the three lists in the main scene: */

    private ListView<Pizza> pizzaList;
    private ListView<Topping> pizzaToppingList;
    private ListView<Topping> toppingList;

    /* Here are the vital constructs required for accessing/organising all the data: */

    private DatabaseConnection database;
    private ArrayList<Topping> allToppings = new ArrayList<>();
    private ArrayList<PizzaTopping> currentToppings = new ArrayList<>();

    /**
     * Here is the constructor for the MainController class which receives references
     * to the three lists in the main scene, sets up the database connection and
     * finally populates the lists which the initial data.
     */

    public MainController(ListView<Pizza> pizzaList, ListView<Topping> pizzaToppingList, ListView<Topping> toppingList) {

        System.out.println("Initialising main controller...");

        this.pizzaList = pizzaList;
        this.pizzaToppingList = pizzaToppingList;
        this.toppingList = toppingList;

        database = new DatabaseConnection("PizzaDatabase.db");
        updateLists(0, 0);

    }

    /**
     * This method needs to be called anytime the lists are changed so that the
     * view is always up to date.
     */

    @SuppressWarnings("Duplicates")
    public void updateLists(int selectedPizzaId, int selectedToppingId) {

        allToppings.clear();
        ToppingService.selectAll(allToppings, database);
        toppingList.setItems(FXCollections.observableArrayList(allToppings));

        pizzaList.getItems().clear();
        PizzaService.selectAll(pizzaList.getItems(), database);

        if (selectedPizzaId != 0) {
            for (int n = 0; n < pizzaList.getItems().size(); n++) {
                if (pizzaList.getItems().get(n).getId() == selectedPizzaId) {
                    pizzaList.getSelectionModel().select(n);
                    pizzaList.getFocusModel().focus(n);
                    pizzaList.scrollTo(n);
                    break;
                }
            }
        }

        if (selectedToppingId != 0) {
            for (int n = 0; n < toppingList.getItems().size(); n++) {
                if (toppingList.getItems().get(n).getId() == selectedToppingId) {
                    toppingList.getSelectionModel().select(n);
                    toppingList.getFocusModel().focus(n);
                    toppingList.scrollTo(n);
                    break;
                }
            }
        }
    }

    /**
     * This method is called every time the selected pizza is changed. It loads the
     * pizza's toppings and makes sure the applied and available toppings lists are
     * correctly populated.
     */

    public void pizzaSelected(Pizza selectedPizza)
    {
        currentToppings.clear();

        ArrayList<Integer> currentToppingIds = new ArrayList<>();

        if (selectedPizza != null) {
            PizzaService.selectPizzaToppings(selectedPizza, currentToppings, database);
            for (PizzaTopping pt : currentToppings) {
                currentToppingIds.add(pt.getToppingId());
            }
        }

        pizzaToppingList.getItems().clear();
        toppingList.getItems().clear();

        for (Topping t: allToppings) {
            if (currentToppingIds.contains(t.getId())) {
                pizzaToppingList.getItems().add(t);
            }
            else {
                toppingList.getItems().add(t);
            }
        }

    }

    /**
     * This displays a dialog box asking for the name of a new pizza and then creates it.
     */

    public void createNewPizza() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create new pizza");
        dialog.setHeaderText(null);
        dialog.setContentText("Pizza's name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            Pizza newPizza = new Pizza(0, result.get());
            PizzaService.save(newPizza, database);

            Topping selectedTopping = toppingList.getSelectionModel().getSelectedItem();
            updateLists(database.lastNewId(), selectedTopping != null ? selectedTopping.getId() : 0);
        }

    }

    /**
     * This displays a confirmation box before subsequently deleting the selected pizza.
     */

    public void deletePizza() {

        Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete pizza");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedPizza + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            PizzaService.deleteById(selectedPizza.getId(), database);
            PizzaService.deletePizzaToppingsByPizzaId(selectedPizza.getId(), database);

            Topping selectedTopping = toppingList.getSelectionModel().getSelectedItem();
            updateLists(0, selectedTopping != null ? selectedTopping.getId() : 0);
        }

    }

    /**
     * This displays a dialog box asking for the name of a new topping and then creates it.
     */

    public void createNewTopping() {


        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create new topping");
        dialog.setHeaderText(null);
        dialog.setContentText("Topping's name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            Topping newTopping = new Topping(0, result.get());
            ToppingService.save(newTopping, database);

            Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();
            updateLists(selectedPizza != null ? selectedPizza.getId() : 0, database.lastNewId());
        }

    }

    /**
     * This displays a confirmation box before subsequently deleting the selected topping.
     */

    public void deleteTopping() {

        Topping selectedTopping = toppingList.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete topping");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedTopping + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ToppingService.deleteById(selectedTopping.getId(), database);
            ToppingService.deletePizzaToppingsByToppingId(selectedTopping.getId(), database);

            Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();
            updateLists(selectedPizza != null ? selectedPizza.getId() : 0, 0);
        }

    }

    /**
     * This creates a new entry in the many-to-many PizzaTopping table.
     */

    public void applyTopping() {

        Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();
        Topping selectedTopping = toppingList.getSelectionModel().getSelectedItem();
        if (selectedPizza == null || selectedTopping == null) return;

        PizzaTopping newTopping = new PizzaTopping(selectedPizza.getId(), selectedTopping.getId());
        PizzaService.savePizzaTopping(newTopping, database);
        updateLists(selectedPizza != null ? selectedPizza.getId() : 0, 0);

    }

    /**
     * This deletes a new entry from the many-to-many PizzaTopping table.
     */

    public void removeTopping() {

        Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();
        Topping selectedPizzaTopping = pizzaToppingList.getSelectionModel().getSelectedItem();
        if (selectedPizza == null || selectedPizzaTopping == null) return;

        PizzaService.deletePizzaTopping(selectedPizza.getId(), selectedPizzaTopping.getId(), database);
        updateLists(selectedPizza != null ? selectedPizza.getId() : 0, selectedPizzaTopping.getId());

    }

    /**
     * This displays a confirmation dialog box and if appropriate closes the database connection and exits.
     */

    public void exitPrompt(WindowEvent we) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Pizza Project");
        alert.setHeaderText("Are you sure you want to exit?");

        Optional result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            database.disconnect();
            System.exit(0);
        } else {
            we.consume();
        }

    }
}
