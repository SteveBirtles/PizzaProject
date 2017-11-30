import Controller.MainController;
import Model.Pizza;
import Model.Topping;
import Model.ToppingView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private static MainController controller;

    private static ListView<Pizza> pizzaList = new ListView<>();
    private static ListView<Topping> pizzaToppingList = new ListView<>();
    private static TableView<ToppingView> toppingTable = new TableView<>();

    /**
     * Here is the start point of the program.
     * A controller object is instantiated and the JavaFX process is launched.
     */

    public static void main(String[] args) {

        controller = new MainController(pizzaList, pizzaToppingList, toppingTable);

        launch(args);
    }

    /**
     * This is the method automatically called by the JavaFX process to create
     * the stage and the scene and populate them. The events (e.g. button clicks)
     * are all wired up to call the appropriate controller methods.
     */

    @Override
    public void start(Stage stage) throws Exception {

        /* First, create the root BorderPane for the scene and set up the stage. */

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add("Resources/stylesheet.css");
        stage.setTitle("Pizza Project");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent we) -> controller.exitPrompt(we));
        stage.show();

        /* Top section of root BorderPane, containing the title. */

        HBox topPane = new HBox(70);
        topPane.setPadding(new Insets(20));
        ImageView pizzaImage1 = new ImageView(new Image("Resources/pizza1.png"));
        topPane.getChildren().add(pizzaImage1);
        Label titleLabel = new Label("Pizza Designer");
        titleLabel.getStyleClass().add("title");
        topPane.getChildren().add(titleLabel);
        root.setTop(topPane);
        topPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(topPane, Pos.TOP_CENTER);
        ImageView pizzaImage2 = new ImageView(new Image("Resources/pizza2.png"));
        topPane.getChildren().add(pizzaImage2);

        /* Left section of root BorderPane, containing the list of pizzas. */

        VBox leftPane = new VBox(20);
        leftPane.setPadding(new Insets(30));
        Label pizzaHeading = new Label("Pizzas:");
        pizzaHeading.getStyleClass().add("heading");
        leftPane.getChildren().add(pizzaHeading);
        pizzaList.setPrefWidth(280);
        pizzaList.setPrefHeight(500);
        pizzaList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> controller.pizzaSelected(newValue)
        );
        leftPane.getChildren().add(pizzaList);
        root.setLeft(leftPane);
        leftPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(leftPane, Pos.CENTER_LEFT);

        /* Centre section of root BorderPane, containing the list of toppings on the current pizza. */

        VBox centerPane = new VBox(20);
        centerPane.setPadding(new Insets(30));
        Label pizzaToppingsHeading = new Label("Toppings Applied:");
        pizzaToppingsHeading.getStyleClass().add("heading");
        centerPane.getChildren().add(pizzaToppingsHeading);
        pizzaToppingList.setMaxWidth(230);
        pizzaToppingList.setPrefHeight(400);
        centerPane.getChildren().add(pizzaToppingList);
        Button applyToppingButton = new Button("Apply topping");
        applyToppingButton.getStyleClass().add("add_button");
        applyToppingButton.setOnAction((event) -> controller.applyTopping());
        centerPane.getChildren().add(applyToppingButton);
        Button removeToppingButton = new Button("Remove topping");
        removeToppingButton.getStyleClass().add("delete_button");
        removeToppingButton.setOnAction((event) -> controller.removeTopping());
        centerPane.getChildren().add(removeToppingButton);
        root.setCenter(centerPane);
        centerPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(centerPane, Pos.CENTER);

        /* Right section of root BorderPane, containing the table of toppings available. */

        VBox rightPane = new VBox(20);
        rightPane.setPadding(new Insets(30));
        Label toppingHeading = new Label("Available Toppings:");
        toppingHeading.getStyleClass().add("heading");
        rightPane.getChildren().add(toppingHeading);
        toppingTable.setPrefWidth(330);
        toppingTable.setPrefHeight(500);
        rightPane.getChildren().add(toppingTable);
        root.setRight(rightPane);
        rightPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(rightPane, Pos.CENTER_RIGHT);

        TableColumn<ToppingView, String> toppingNameColumn = new TableColumn<>("Topping");
        toppingNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        toppingNameColumn.prefWidthProperty().bind(toppingTable.widthProperty().multiply(0.63));
        toppingNameColumn.setResizable(false);
        toppingTable.getColumns().add(toppingNameColumn);

        TableColumn<ToppingView, String> toppingTypeColumn = new TableColumn<>("Type");
        toppingTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        toppingTypeColumn.prefWidthProperty().bind(toppingTable.widthProperty().multiply(0.31));
        toppingTypeColumn.setResizable(false);
        toppingTable.getColumns().add(toppingTypeColumn);

        /* Bottom section of root BorderPane, containing the buttons to create and delete pizzas and toppings. */

        HBox bottomPane = new HBox(20);
        bottomPane.setAlignment(Pos.CENTER_LEFT);
        bottomPane.setPadding(new Insets(30));
        HBox bottomPaneRight = new HBox(20);
        HBox.setHgrow(bottomPaneRight, Priority.ALWAYS);
        bottomPaneRight.setAlignment(Pos.CENTER_RIGHT);
        Button addPizzaButton = new Button("Create new pizza");
        addPizzaButton.getStyleClass().add("add_button");
        addPizzaButton.setOnAction((event) -> controller.createNewPizza());
        bottomPane.getChildren().add(addPizzaButton);
        Button deletePizzaButton = new Button("Delete pizza");
        deletePizzaButton.getStyleClass().add("delete_button");
        deletePizzaButton.setOnAction((event) -> controller.deletePizza());
        bottomPane.getChildren().add(deletePizzaButton);
        Button addToppingButton = new Button("Create new topping");
        addToppingButton.getStyleClass().add("add_button");
        addToppingButton.setOnAction((event) -> controller.createNewTopping());
        bottomPaneRight.getChildren().add(addToppingButton);
        Button deleteToppingButton = new Button("Delete topping");
        deleteToppingButton.getStyleClass().add("delete_button");
        deleteToppingButton.setOnAction((event) -> controller.deleteTopping());
        bottomPaneRight.getChildren().add(deleteToppingButton);
        bottomPane.getChildren().add(bottomPaneRight);
        root.setBottom(bottomPane);
        BorderPane.setAlignment(bottomPane, Pos.BOTTOM_CENTER);

    }

}