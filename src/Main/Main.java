package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {
    private Inventory inventory = new Inventory();

    @Override
    public void start(Stage primaryStage) throws Exception{

        // setup the grid
        GridPane gridpane = new GridPane();

        // setup the parts table
        Label partsTableLabel = new Label("Parts");
        TableView<Part> partsTable = getPartsTable();
        gridpane.add(partsTableLabel, 0, 0);
        gridpane.add(partsTable, 0, 1);

        // Set the gap sizes.
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setPadding(new Insets(30));

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(gridpane, 1024, 768));
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
