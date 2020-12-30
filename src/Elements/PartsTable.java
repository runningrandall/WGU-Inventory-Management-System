package Elements;
import Main.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
  private Inventory inventory;
  private TableView<Part> partsTable;
  private final int defaultPadding = 10;
  private final String inHouseLabel = "In House";
  private final String outsourcedLabel = "Outsourced";
  private final String costRegex = "^[0-9]+.[0-9]{2}$";

  public PartsTable(Inventory inventory, TableView<Part> partsTable) {
    this.inventory = inventory;
    this.partsTable = partsTable;
  }

  public HBox getPartsHeader() {
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
        showPartsForm();
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

  private void showPartsForm() {
    RadioButton inHouseRb = new RadioButton(inHouseLabel);
    RadioButton outsourcedRb = new RadioButton(outsourcedLabel);
    TextField partIdTf = new TextField();
    TextField partNameTf = new TextField();
    TextField partInvTf = new TextField();
    TextField partCostTf = new TextField();
    TextField partMaxTf = new TextField();
    TextField partMinTf = new TextField();
    TextField partMachineIdTf = new TextField();
    TextField partCompanyNameTf = new TextField();

    Dialog<String> addPartDialog = new Dialog<String>();
    addPartDialog.setTitle("Add Part");
    addPartDialog.setResizable(true);

    // in house vs outsourced part
    Label partTypeLabel = new Label("Part Source"); // label
    ToggleGroup partSourceGroup = new ToggleGroup();
    inHouseRb.setToggleGroup(partSourceGroup);
    outsourcedRb.setToggleGroup(partSourceGroup);

    // setup the form
    // part id
    partIdTf.setPromptText("Auto Gen - Disabled");
    partIdTf.setDisable(true); // WHY SHOW IT?
    Label partIdLabel = new Label("ID"); // label

    // part name
    partNameTf.setPromptText("Part Name");
    Label partNameLabel = new Label("Name"); // label

    // part inventory
    partInvTf.setPromptText("0");
    Label partInventoryLabel = new Label("Inventory"); // label
    // force the field to be numeric only
    partInvTf.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldV, String newV) {
        if (!newV.matches("\\d*")) {
          partInvTf.setText(newV.replaceAll("[^\\d]", ""));
        }
      }
    });

    // part cost
    partCostTf.setPromptText("0.00");
    Label partCostLabel = new Label("Price/Cost"); // label
    partCostTf.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldV, String newV) {
        if (!newV.matches(costRegex)){
          partCostTf.setText(newV.replaceAll("[" + costRegex + "]", ""));
        }
      }
    });

    // part max
    partMaxTf.setPromptText("20");
    Label partMaxLabel = new Label("Max"); // label
    partMaxTf.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldV, String newV) {
        if (!newV.matches("\\d*")) {
          partMaxTf.setText(newV.replaceAll("[^\\d]", ""));
        }
      }
    });

    // part min
    partMinTf.setPromptText("1");
    Label partMinLabel = new Label("Min"); // label
    partMinTf.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldV, String newV) {
        if (!newV.matches("\\d*")) {
          partMinTf.setText(newV.replaceAll("[^\\d]", ""));
        }
      }
    });

    // part machine id
    partMachineIdTf.visibleProperty().bind(inHouseRb.selectedProperty());
    partMachineIdTf.setPromptText("0000");
    Label partMachineIdLabel = new Label("Machine ID"); // label
    partMachineIdLabel.visibleProperty().bind(inHouseRb.selectedProperty());
    partMachineIdTf.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldV, String newV) {
        if (!newV.matches("\\d*")) {
          partMachineIdTf.setText(newV.replaceAll("[^\\d]", ""));
        }
      }
    });

    // part company name
    partCompanyNameTf.visibleProperty().bind(outsourcedRb.selectedProperty());
    partCompanyNameTf.setPromptText("Company");
    Label partCompanyNameLabel = new Label("Company Name"); // label
    partCompanyNameLabel.visibleProperty().bind(outsourcedRb.selectedProperty());



    // setup the grid
    GridPane gridpane = new GridPane();
    ColumnConstraints constraints = new ColumnConstraints();
    constraints.setHgrow(Priority.ALWAYS);
    gridpane.getColumnConstraints().addAll(new ColumnConstraints(), constraints);
    gridpane.add(partTypeLabel, 0, 0);
    gridpane.add(inHouseRb, 1, 0);
    gridpane.add(outsourcedRb, 1, 1);
    gridpane.add(partIdLabel, 0, 2);
    gridpane.add(partIdTf, 1, 2);
    gridpane.add(partNameLabel, 0, 3);
    gridpane.add(partNameTf, 1, 3);
    gridpane.add(partInventoryLabel, 0, 4);
    gridpane.add(partInvTf, 1, 4);
    gridpane.add(partCostLabel, 0, 5);
    gridpane.add(partCostTf, 1, 5);
    gridpane.add(partMaxLabel, 0, 6);
    gridpane.add(partMaxTf, 1, 6);
    gridpane.add(partMinLabel, 2, 6);
    gridpane.add(partMinTf, 3, 6);
    gridpane.add(partMachineIdLabel, 0, 7);
    gridpane.add(partMachineIdTf, 1, 7);
    gridpane.add(partCompanyNameLabel, 0, 7);
    gridpane.add(partCompanyNameTf, 1, 7);
    gridpane.setVgap(10);
    gridpane.setHgap(10);
    gridpane.setPadding(new Insets(defaultPadding * 3));
    // set content
    addPartDialog.getDialogPane().setContent(gridpane);
    // add dialog buttons
    addPartDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
    // tap into event listener
    final Button saveButton = (Button) addPartDialog.getDialogPane().lookupButton(ButtonType.OK);
    saveButton.addEventFilter(
      ActionEvent.ACTION,
      event -> {
        try {
          // process the form here
          // make sure it is valid
          ObservableList<Part> allParts = inventory.getAllParts();
          Integer newPartId = 0;
          for(Part part : allParts) {
            if(newPartId <= part.getId()) {
              newPartId = part.getId() + 1;
            }
          }

          Boolean partSourceValid = inHouseRb.isSelected() || outsourcedRb.isSelected();
          String partSource = partSourceValid && inHouseRb.isSelected() ? inHouseLabel : outsourcedLabel;
          String partName = partNameTf.getText();
          Boolean partNameValid = partName.length() > 0;
          Integer partInventory = Integer.parseInt(partInvTf.getText());
          Boolean partInventoryValid = partInventory >= 0;
          Double partCost = Double.parseDouble(partCostTf.getText());
          Boolean partCostValid = partCost >= 0;
          Integer partMax = Integer.parseInt(partMaxTf.getText());
          Boolean partMaxValid = partMax >= 1;
          Integer partMin = Integer.parseInt(partMinTf.getText());
          Boolean partMinValid = partMin >= 0;
          Boolean partMinMaxValid = partMaxValid && partMinValid && partMax >= partMin;
          Integer partMachineId = inHouseRb.isSelected() ? Integer.parseInt(partMachineIdTf.getText()) : null;
          String partCompanyName = outsourcedRb.isSelected() ? partCompanyNameTf.getText() : null;

          Boolean partMachineOrCompanyNameValid =
            (inHouseRb.isSelected() && partMachineId != null && partMachineId > 999)
            ||
            (outsourcedRb.isSelected() && partCompanyName.length() > 0);

          if (
            partSourceValid
              && partNameValid
              && partInventoryValid
              && partCostValid
              && partMinMaxValid
              && partMachineOrCompanyNameValid) {
            if (outsourcedRb.isSelected()) {
              Outsourced newPart = new Outsourced(newPartId, partName, partCost, partInventory, partMax, partMin, partCompanyName);
              inventory.addPart(newPart);
            } else {
              InHouse newPart = new InHouse(newPartId, partName, partCost, partInventory, partMax, partMin, partMachineId);
              inventory.addPart(newPart);
            }
            addPartDialog.close();
          } else {
            System.out.println("Not Valid!");
            // TODO: handle this
            event.consume();
          }
        } catch (Exception e) {
          // do something here
          System.out.println(e.toString());
          // TODO: handle this
          event.consume();
        }
      }
    );
    addPartDialog.showAndWait();
    addPartDialog.close();
    addPartDialog.hide();
  }
}
