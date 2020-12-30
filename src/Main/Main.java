package Main;
import Elements.PartsTable;

import Elements.ProductsTable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {
    private final Inventory inventory = new Inventory();
    private final int defaultPadding = 10;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // setup the grid
        GridPane gridpane = new GridPane();

        // partsTable elements
        PartsTable partsTableE = new PartsTable(); // get elements
        TableView<Part> partsTable = getPartsTable(); // the parts table itself
        HBox partsHeader = partsTableE.getPartsHeader(partsTable, inventory); // get header
        HBox partsFooter = partsTableE.getPartsFooter();
        // add to grid
        gridpane.add(partsHeader, 0, 0);
        gridpane.add(partsTable, 0, 1);
        gridpane.add(partsFooter, 0, 2);

        // productsTable elements
        ProductsTable productsTableE = new ProductsTable(); // get elements
        TableView<Product> productsTable = getProductsTable(); // get products table
        HBox productsHeader = productsTableE.getProductsHeader(productsTable, inventory); // get header
        HBox productsFooter = productsTableE.getProductsFooter();
        // add to grid
        gridpane.add(productsHeader, 1, 0);
        gridpane.add(productsTable, 1, 1);
        gridpane.add(productsFooter, 1, 2);

        // exit button
        Button exitBtn = new Button();
        exitBtn.setText("Exit");
        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

        gridpane.add(exitBtn, 1, 3);
        gridpane.setHalignment(exitBtn, HPos.RIGHT);

        // Set the gap sizes.
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setPadding(new Insets(defaultPadding * 3));

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(gridpane));
        primaryStage.show();
    }


    /**
     * method to get the default parts table
     * @return TableView of Parts
     */
    private TableView<Part> getPartsTable() {
        // setup the inventory
        // this has to be done before loading the table
        loadDefaultParts();

        TableView<Part> partsTable = new TableView<>();
        //Creating columns
        TableColumn partIdCol = new TableColumn("Part ID");
         partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn partNameCol = new TableColumn("Part Name");
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn partStockCol = new TableColumn("Inventory Level");
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn partPriceCol = new TableColumn("Price/Cost Per Item");
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(inventory.getAllParts());
        partsTable.getColumns().addAll(partIdCol, partNameCol, partStockCol, partPriceCol);
        partsTable.setMaxSize(350, 200);
        return partsTable;
    }

    /**
     * method to get the default products table
     * @return TableView of Products
     */
    private TableView<Product> getProductsTable() {
        loadDefaultProducts();

        TableView<Product> productsTable = new TableView<>();
        //Creating columns
        TableColumn productsIdCol = new TableColumn("Product ID");
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn productNameCol = new TableColumn("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn productStockCol = new TableColumn("Inventory Level");
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn productPriceCol = new TableColumn("Price/Cost Per Item");
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(inventory.getAllProducts());
        productsTable.getColumns().addAll(productsIdCol, productNameCol, productStockCol, productPriceCol);
        productsTable.setMaxSize(350, 200);
        return productsTable;
    }

    private void loadDefaultProducts() {
        inventory.addProduct(new Product(1000, "Giant Bike", 299.99, 5, 1, 10));
        inventory.addProduct(new Product(1001, "Tricyle", 99.99, 3, 1, 5));
    }
    /**
     * method to load some default parts
     */
    private void loadDefaultParts () {
        // load data into table
        inventory.addPart(new Part(1,"Brakes", 15.00, 10, 1, 10));
        inventory.addPart(new Part(2,"Wheel", 11.00, 16, 1, 10));
        inventory.addPart(new Part(3,"Seat", 15.00, 10, 1, 10));
    }
    public static void main(String[] args) {
        launch(args);
    }
}
