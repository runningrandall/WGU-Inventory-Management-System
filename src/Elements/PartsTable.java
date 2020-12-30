package Elements;
import Main.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PartsTable {
    private final int defaultPadding = 10;
    private final String inHouseLabel = "In House";
    private final String outsourcedLabel = "Outsourced";

    public HBox getPartsHeader(TableView<Part> partsTable, Inventory inventory) {
        // setup the parts table
        Label partsTableLabel = new Label("Parts"); // label
        TextField partsTableSearch = new TextField(); // search field
        partsTableSearch.setPromptText("Search by Part ID or Name"); // placehoder
        partsTableSearch.addEventHandler(KeyEvent.KEY_RELEASED, event -> { // event handler
            String searchText = partsTableSearch.getText();
            if (searchText.length() == 0) {
                partsTable.setItems(inventory.getAllParts());
            }
            try {
                int partId = Integer.parseInt(searchText);
                partsTable.setItems(FXCollections.observableArrayList(inventory.lookupPart(partId)));
            } catch (Exception e) {
                partsTable.setItems(inventory.lookupPart(searchText));
            }
        });
        // setup containing hbox for label/search field
        HBox partsHeader = new HBox(defaultPadding * 5, partsTableLabel, partsTableSearch);
        partsHeader.setAlignment(Pos.BASELINE_LEFT); // alignment
        HBox.setMargin(partsTableSearch, new Insets(0, 0, 0, 100)); // move it to the right

        return partsHeader;
    }

    public HBox getPartsFooter() {
        Button partsAddBtn = new Button();
        partsAddBtn.setText("Add");
        partsAddBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<String> addPartDialog = getAddPartDialog();
                addPartDialog.showAndWait();

            }
        });

        Button partsModifyBtn = new Button();
        partsModifyBtn.setText("Modify");

        Button partsDeleteBtn = new Button();
        partsDeleteBtn.setText("Delete");

        HBox partsFooter = new HBox(defaultPadding, partsAddBtn, partsModifyBtn, partsDeleteBtn);
        partsFooter.setAlignment(Pos.BASELINE_RIGHT); // alignment
        return partsFooter;
    }

    private Dialog<String> getAddPartDialog() {
        Dialog<String> addPartDialog = new Dialog<String>();
        addPartDialog.setTitle("Add Part");
        addPartDialog.setResizable(true);
        // setup the grid
        GridPane gridpane = new GridPane();
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(new ColumnConstraints(), constraints);

        // in house vs outsourced part
        Label partTypeLabel = new Label("Part Source"); // label
        ToggleGroup partSourceGroup = new ToggleGroup();
        RadioButton inHouseRb = new RadioButton(inHouseLabel);
        inHouseRb.setToggleGroup(partSourceGroup);
        RadioButton outsourcedRb = new RadioButton(outsourcedLabel);
        outsourcedRb.setToggleGroup(partSourceGroup);
        gridpane.add(partTypeLabel, 0, 0);
        gridpane.add(inHouseRb, 1, 0);
        gridpane.add(outsourcedRb, 1, 1);


        // setup the form
        // part id
        TextField partIdTf = new TextField(); // search field
        partIdTf.setPromptText("Auto Gen - Disabled");
        partIdTf.setDisable(true);
        Label partIdLabel = new Label("ID"); // label
        gridpane.add(partIdLabel, 0, 2);
        gridpane.add(partIdTf, 1, 2);

        // part name
        TextField partNameTf = new TextField(); // search field
        partNameTf.setPromptText("Part Name");
        Label partNameLabel = new Label("Name"); // label
        gridpane.add(partNameLabel, 0, 3);
        gridpane.add(partNameTf, 1, 3);

        // part inventory
        TextField partNameInv = new TextField(); // search field
        partNameInv.setPromptText("0");
        Label partInventoryLabel = new Label("Inventory"); // label
        gridpane.add(partInventoryLabel, 0, 4);
        gridpane.add(partNameInv, 1, 4);

        // part cost
        TextField partCostTf = new TextField(); // search field
        partCostTf.setPromptText("0.00");
        Label partCostLabel = new Label("Price/Cost"); // label
        gridpane.add(partCostLabel, 0, 5);
        gridpane.add(partCostTf, 1, 5);

        // part max
        TextField partMaxTf = new TextField(); // search field
        partMaxTf.setPromptText("20");
        Label partMaxLabel = new Label("Max"); // label
        gridpane.add(partMaxLabel, 0, 6);
        gridpane.add(partMaxTf, 1, 6);

        // part min
        TextField partMinTf = new TextField(); // search field
        partMinTf.setPromptText("1");
        Label partMinLabel = new Label("Min"); // label
        gridpane.add(partMinLabel, 2, 6);
        gridpane.add(partMinTf, 3, 6);

        // part machine id
        TextField partMachineIdTf = new TextField(); // search field
        partMachineIdTf.visibleProperty().bind(inHouseRb.selectedProperty());
        partMachineIdTf.setPromptText("0000");
        Label partMachineIdLabel = new Label("Machine ID"); // label
        partMachineIdLabel.visibleProperty().bind(inHouseRb.selectedProperty());
        gridpane.add(partMachineIdLabel, 0, 7);
        gridpane.add(partMachineIdTf, 1, 7);

        // part machine id
        TextField partCompanyNameTf = new TextField(); // search field
        partCompanyNameTf.visibleProperty().bind(outsourcedRb.selectedProperty());
        partCompanyNameTf.setPromptText("Company");
        Label partCompanyNameLabel = new Label("Company Name"); // label
        partCompanyNameLabel.visibleProperty().bind(outsourcedRb.selectedProperty());
        gridpane.add(partCompanyNameLabel, 0, 7);
        gridpane.add(partCompanyNameTf, 1, 7);

        // Set the gap sizes.
        gridpane.setVgap(10);
        gridpane.setHgap(10);
        gridpane.setPadding(new Insets(defaultPadding * 3));
        addPartDialog.getDialogPane().setContent(gridpane);
        addPartDialog.getDialogPane().getButtonTypes().add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));




        return addPartDialog;
    }
}
