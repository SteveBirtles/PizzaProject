import Model.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {

    public static DatabaseConnection database;

    private static ListView<Pizza> pizzaList;
    private static ListView<Topping> pizzaToppingList;
    private static ListView<Topping> toppingList;

    private static ArrayList<Topping> allToppings;
    private static ArrayList<PizzaTopping> currentToppings;

    @Override
    public void start(Stage stage) throws Exception {

        database = new DatabaseConnection("PizzaDatabase.db");
        currentToppings = new ArrayList<>();
        allToppings = new ArrayList<>();

        BorderPane root = new BorderPane();

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add("stylesheet.css");

        stage.setTitle("Pizza Project");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent we) -> exitPrompt(we));
        stage.show();

        VBox topPane = new VBox(20);
        topPane.setPadding(new Insets(20));
        Label titleLabel = new Label("Pizza Project");
        titleLabel.getStyleClass().add("title");
        topPane.getChildren().add(titleLabel);
        root.setTop(topPane);
        topPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(topPane, Pos.TOP_CENTER);

        VBox leftPane = new VBox(20);
        leftPane.setPadding(new Insets(20));
        Label pizzaHeading = new Label("Pizzas:");
        pizzaHeading.getStyleClass().add("heading");
        leftPane.getChildren().add(pizzaHeading);
        pizzaList = new ListView<>();
        pizzaList.setPrefWidth(280);
        pizzaList.setPrefHeight(500);

        pizzaList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> pizzaSelected(newValue));

        leftPane.getChildren().add(pizzaList);
        root.setLeft(leftPane);
        leftPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(leftPane, Pos.CENTER_LEFT);

        VBox centerPane = new VBox(20);
        centerPane.setPadding(new Insets(20));
        Label pizzaToppingsHeading = new Label("Toppings Applied:");
        pizzaToppingsHeading.getStyleClass().add("heading");
        centerPane.getChildren().add(pizzaToppingsHeading);
        pizzaToppingList = new ListView<>();
        pizzaToppingList.setMaxWidth(280);
        pizzaToppingList.setPrefHeight(400);
        centerPane.getChildren().add(pizzaToppingList);
        Button applyToppingButton = new Button("Apply topping");
        applyToppingButton.getStyleClass().add("add_button");
        applyToppingButton.setOnAction((event) -> applyTopping());
        centerPane.getChildren().add(applyToppingButton);
        Button removeToppingButton = new Button("Remove topping");
        removeToppingButton.getStyleClass().add("delete_button");
        removeToppingButton.setOnAction((event) -> removeTopping());
        centerPane.getChildren().add(removeToppingButton);
        root.setCenter(centerPane);
        centerPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(centerPane, Pos.CENTER);

        VBox rightPane = new VBox(20);
        rightPane.setPadding(new Insets(20));
        Label toppingHeading = new Label("Available Toppings:");
        toppingHeading.getStyleClass().add("heading");
        rightPane.getChildren().add(toppingHeading);
        toppingList = new ListView<>();
        toppingList.setPrefWidth(280);
        toppingList.setPrefHeight(500);
        toppingList.setItems(FXCollections.observableArrayList(allToppings));
        rightPane.getChildren().add(toppingList);
        root.setRight(rightPane);
        rightPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(rightPane, Pos.CENTER_RIGHT);

        HBox bottomPane = new HBox(20);
        bottomPane.setAlignment(Pos.CENTER_LEFT);
        bottomPane.setPadding(new Insets(20));

        HBox bottomPaneRight = new HBox(20);
        HBox.setHgrow(bottomPaneRight, Priority.ALWAYS);
        bottomPaneRight.setAlignment(Pos.CENTER_RIGHT);

        Button addPizzaButton = new Button("Create new pizza");
        addPizzaButton.getStyleClass().add("add_button");
        addPizzaButton.setOnAction((event) -> createNewPizza());
        bottomPane.getChildren().add(addPizzaButton);

        Button deletePizzaButton = new Button("Delete selected pizza");
        deletePizzaButton.getStyleClass().add("delete_button");
        deletePizzaButton.setOnAction((event) -> deletePizza());
        bottomPane.getChildren().add(deletePizzaButton);

        Button addToppingButton = new Button("Create new topping");
        addToppingButton.getStyleClass().add("add_button");
        addToppingButton.setOnAction((event) -> createNewTopping());
        bottomPaneRight.getChildren().add(addToppingButton);

        Button deleteToppingButton = new Button("Delete selected topping");
        deleteToppingButton.getStyleClass().add("delete_button");
        deleteToppingButton.setOnAction((event) -> deleteTopping());
        bottomPaneRight.getChildren().add(deleteToppingButton);

        bottomPane.getChildren().add(bottomPaneRight);
        root.setBottom(bottomPane);
        BorderPane.setAlignment(bottomPane, Pos.BOTTOM_CENTER);

        updateLists(0, 0);

    }

    public static void main(String[] args) {

        launch(args);

    }

    @SuppressWarnings("Duplicates")
    public static void updateLists(int selectedPizzaId, int selectedToppingId) {

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

    public static void pizzaSelected(Pizza selectedPizza)
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

    public static void createNewPizza() {

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

    public static void deletePizza() {

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

    public static void createNewTopping() {


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

    public static void deleteTopping() {

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

    public static void applyTopping() {
        Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();
        Topping selectedTopping = toppingList.getSelectionModel().getSelectedItem();
        if (selectedPizza == null || selectedTopping == null) return;
        PizzaTopping newTopping = new PizzaTopping(selectedPizza.getId(), selectedTopping.getId());
        PizzaService.savePizzaTopping(newTopping, database);
        updateLists(selectedPizza != null ? selectedPizza.getId() : 0, 0);
    }

    public static void removeTopping() {
        Pizza selectedPizza = pizzaList.getSelectionModel().getSelectedItem();
        Topping selectedPizzaTopping = pizzaToppingList.getSelectionModel().getSelectedItem();
        if (selectedPizza == null || selectedPizzaTopping == null) return;
        PizzaService.deletePizzaTopping(selectedPizza.getId(), selectedPizzaTopping.getId(), database);
        updateLists(selectedPizza != null ? selectedPizza.getId() : 0, selectedPizzaTopping.getId());
    }

    public static void exitPrompt(WindowEvent we) {

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