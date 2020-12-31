package Elements;
import Main.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.Optional;

/**
 * PartsTable class implements the UI components and logic that
 * allow parts to be added, modified, and deleted
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/31/2020
 */
public class PartsTable {
  private final Inventory inventory;
  private final TableView<Part> partsTable;
  private final int defaultPadding = 10;
  private final String costRegex = "^[0-9]+.[0-9]{2}$";
  // create a alert
  private final Alert a = new Alert(Alert.AlertType.NONE);

  /**
   * PartsTable constructor
   * @param inventory - the current inventory from main
   * @param partsTable - the partsTable from main
   */
  public PartsTable(Inventory inventory, TableView<Part> partsTable) {
    this.inventory = inventory;
    this.partsTable = partsTable;
  }

  /**
   * Method to get the parts header for display in the main scene
   * @return HBox
   */
  public HBox getPartsHeader() {
    // setup the parts table
    Label partsTableLabel = new Label("Parts"); // label
    TextField partsTableSearch = new TextField(); // search field
    partsTableSearch.setPromptText("Search by Part ID or Name"); // placeholder
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
    // setup containing horizontal box for label/search field
    HBox partsHeader = new HBox(defaultPadding * 5, partsTableLabel, partsTableSearch);
    partsHeader.setAlignment(Pos.BASELINE_LEFT); // alignment
    HBox.setMargin(partsTableSearch, new Insets(0, 0, 0, 100)); // move it to the right

    return partsHeader;
  }

  /**
   * Method to return the parts footer for display in the main scene
   * It also contains the buttons and button event listeners for
   * adding/modifying/deleting parts
   * @return HBox
   */
  public HBox getPartsFooter() {
    Button partsAddBtn = new Button("Add");
    partsAddBtn.setOnAction(actionEvent -> showPartForm(null));

    Button partsModifyBtn = new Button("Modify");
    partsModifyBtn.setOnAction(actionEvent -> {
      Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
      if (selectedPart == null) {
        showError("Please select a part to modify.");
      } else {
        showPartForm(selectedPart.getId());
      }
    });

    Button partsDeleteBtn = new Button("Delete");
    partsDeleteBtn.setOnAction(actionEvent -> {
      Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
      if (selectedPart == null) {
        showError("Please select a part to delete.");
      } else {
        // must confirm the delete via confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete a Part");
        alert.setContentText("Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        // if the alert is confirmed then delete the part, otherwise close the alert
        if (result.isPresent() && result.get() == ButtonType.OK){
          if(!inventory.deletePart(selectedPart)) {
            showError("There was an error deleting the part. Please try again later.");
          };
        } else {
          alert.close();
        }
      }
    });

    HBox partsFooter = new HBox(defaultPadding, partsAddBtn, partsModifyBtn, partsDeleteBtn);
    partsFooter.setAlignment(Pos.BASELINE_RIGHT); // alignment
    return partsFooter;
  }

  /**
   * method to show the parts form itself
   * this form is used to add and modify parts
   * @param partId - the part id we are editing (or null)
   */
  private void showPartForm(Integer partId) {
    // determine if we are editing or adding a new part
    boolean isEditing = partId != null;
    // if editing grab the part
    Part part = isEditing ? inventory.lookupPart(partId) : null;
    // setup various form components
    String inHouseLabel = "In House";
    RadioButton inHouseRb = new RadioButton(inHouseLabel);
    String outsourcedLabel = "Outsourced";
    RadioButton outsourcedRb = new RadioButton(outsourcedLabel);
    TextField partIdTf = new TextField();
    TextField partNameTf = new TextField();
    TextField partInvTf = new TextField();
    TextField partCostTf = new TextField();
    TextField partMaxTf = new TextField();
    TextField partMinTf = new TextField();
    TextField partMachineIdTf = new TextField();
    TextField partCompanyNameTf = new TextField();

    // populate fields if we are editing
    if (isEditing) {
      try {
        // select the part type based on InHouse vs. Outsourced
        if (part instanceof InHouse) {
          partMachineIdTf.setText(Integer.toString(((InHouse) part).getMachineId()));
          inHouseRb.setSelected(true);
        } else if (part instanceof Outsourced) {
          partCompanyNameTf.setText(((Outsourced) part).getCompanyName());
          outsourcedRb.setSelected(true);
        }

        // populate other fields
        partIdTf.setText(Integer.toString(part.getId()));
        partNameTf.setText(part.getName());
        partInvTf.setText(Integer.toString(part.getStock()));
        partCostTf.setText(Double.toString(part.getPrice()));
        partMaxTf.setText(Integer.toString(part.getMax()));
        partMinTf.setText(Integer.toString(part.getMin()));
      } catch (Exception e) {
        showError(e.getMessage());
      }
    }

    // setup the dialog
    Dialog<String> addPartDialog = new Dialog<>();
    addPartDialog.setTitle(isEditing ? "Modify Part" : "Add Part");
    addPartDialog.setResizable(true);

    // in house vs outsourced part
    Label partTypeLabel = new Label("Part Source"); // label
    ToggleGroup partSourceGroup = new ToggleGroup();
    inHouseRb.setToggleGroup(partSourceGroup);
    outsourcedRb.setToggleGroup(partSourceGroup);

    // part id
    partIdTf.setPromptText("Auto Gen - Disabled");
    partIdTf.setDisable(true); // WHY SHOW IT IF ALWAYS DISABLED?
    Label partIdLabel = new Label("ID");

    // part name
    partNameTf.setPromptText("Part Name");
    Label partNameLabel = new Label("Name"); // label

    // part inventory
    partInvTf.setPromptText("0");
    Label partInventoryLabel = new Label("Inventory"); // label
    // force the field to be numeric only
    partInvTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        partInvTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // part cost
    partCostTf.setPromptText("0.00");
    Label partCostLabel = new Label("Price/Cost"); // label
    // force to be double entry only
    partCostTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches(costRegex)){
        partCostTf.setText(newV.replaceAll("[" + costRegex + "]", ""));
      }
    });

    // part max
    partMaxTf.setPromptText("20");
    Label partMaxLabel = new Label("Max"); // label
    // numbers only
    partMaxTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        partMaxTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // part min
    partMinTf.setPromptText("1");
    Label partMinLabel = new Label("Min");
    // numbers only
    partMinTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        partMinTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // part machine id
    partMachineIdTf.visibleProperty().bind(inHouseRb.selectedProperty());
    partMachineIdTf.setPromptText("0000");
    Label partMachineIdLabel = new Label("Machine ID");
    // only show when the part is "InHouse"
    partMachineIdLabel.visibleProperty().bind(inHouseRb.selectedProperty());
    // numbers only
    partMachineIdTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        partMachineIdTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // part company name
    partCompanyNameTf.visibleProperty().bind(outsourcedRb.selectedProperty());
    partCompanyNameTf.setPromptText("Company");
    Label partCompanyNameLabel = new Label("Company Name");
    // only show when the part is "Outsourced"
    partCompanyNameLabel.visibleProperty().bind(outsourcedRb.selectedProperty());

    // setup the grid
    GridPane gridpane = new GridPane();
    ColumnConstraints constraints = new ColumnConstraints();
    constraints.setHgrow(Priority.ALWAYS);
    gridpane.getColumnConstraints().addAll(new ColumnConstraints(), constraints);
    gridpane.setVgap(10);
    gridpane.setHgap(10);
    gridpane.setPadding(new Insets(defaultPadding * 3));
    // would be nice to have a map and loop through these instead of many lines of assignments
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

    // set content
    addPartDialog.getDialogPane().setContent(gridpane);
    // add dialog buttons
    addPartDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
    // tap into event listener
    final Button saveButton = (Button) addPartDialog.getDialogPane().lookupButton(ButtonType.OK);
    // when saving we process the form
    saveButton.addEventFilter(
      ActionEvent.ACTION,
      event -> {
        try {
          // setup validations with series of booleans and values
          boolean partSourceValid = inHouseRb.isSelected() || outsourcedRb.isSelected();
          String partName = partNameTf.getText();
          boolean partNameValid = partName.length() > 0;
          int partInventory = Integer.parseInt(partInvTf.getText());
          double partCost = Double.parseDouble(partCostTf.getText());
          boolean partCostValid = partCost >= 0;
          int partMax = Integer.parseInt(partMaxTf.getText());
          boolean partMaxValid = partMax >= 1;
          int partMin = Integer.parseInt(partMinTf.getText());
          boolean partMinValid = partMin >= 0;
          boolean partMinMaxValid = partMaxValid && partMinValid && partMax >= partMin;
          boolean partInventoryValid = partInventory <= partMax && partInventory >= partMin;
          Integer partMachineId = inHouseRb.isSelected() ? Integer.parseInt(partMachineIdTf.getText()) : null;
          String partCompanyName = outsourcedRb.isSelected() ? partCompanyNameTf.getText() : null;

          boolean partMachineOrCompanyNameValid =
            (inHouseRb.isSelected() && partMachineId != null && partMachineId > 999)
            ||
            (outsourcedRb.isSelected() && partCompanyName!= null && partCompanyName.length() > 0);

          // form validation
          if (!partSourceValid) {
            showError("Please select a part source (InHouse or Outsourced)");
            event.consume();
          } else if (!partNameValid) {
            showError("Please provide a valid part name");
            event.consume();
          } else if (!partInventoryValid) {
            showError("Please provide a valid inventory level. It must be between the min and max");
            event.consume();
          } else if (!partCostValid) {
            showError("Please provide a valid part cost");
            event.consume();
          } else if (!partMinMaxValid) {
            showError("Please provide a valid min/max value.");
            event.consume();
          } else if (!partMachineOrCompanyNameValid) {
            showError("Please provide a valid " + (outsourcedRb.isSelected() ? "company name" : "machine id"));
            event.consume();
          } else {
            int newPartId = partId != null ? partId : getNewPartId();
            Part newPart;
            // various types based on outsourced vs InHouse
            if (outsourcedRb.isSelected()) {
              newPart = new Outsourced(newPartId, partName, partCost, partInventory, partMin, partMax, partCompanyName);
            } else if (inHouseRb.isSelected()) {
              newPart = new InHouse(newPartId, partName, partCost, partInventory, partMin, partMax, partMachineId);
            } else {
              newPart = new Part(newPartId, partName, partCost, partInventory, partMin, partMax);
            }

            if (isEditing) {
              inventory.updatePart(getPartIndexFromPartId(partId), newPart);
            } else {
              inventory.addPart(newPart);
            }
            addPartDialog.close();
          }
        } catch (Exception e) {
          showError(e.getMessage());
          // prevent dialog from closing
          event.consume();
        }
      }
    );
    addPartDialog.showAndWait();
    addPartDialog.close();
    addPartDialog.hide();
  }

  /**
   * method to get the newest part id
   * @return partId
   */
  private int getNewPartId() {
    int partId = 0;
    ObservableList<Part> allParts = inventory.getAllParts();
    for(Part myPart : allParts) {
      if(partId <= myPart.getId()) {
        partId = myPart.getId() + 1;
      }
    }
    return partId;
  }

  /**
   * Method to show an alert error
   * Used when there is an error, validation is missing, etc.
   * @param message - the string message to be displayed in the error
   */
  private void showError(String message) {
    // set alert type
    a.setAlertType(Alert.AlertType.ERROR);
    a.setTitle("Error");
    a.setContentText(message);
    // show the dialog
    a.show();
  }

  /**
   * method to get the part index from a part id
   * it might be better for this method to be in the Inventory class
   * but the UML diagram said not to
   * @param partId - the part id we are looking for
   * @return partIndex
   */
  private int getPartIndexFromPartId(int partId) {
    int editingPartIndex = 0;
    int countingPartIndex = 0;
    ObservableList<Part> allParts = inventory.getAllParts();
    for(Part myPart : allParts) {
      // I'm worried about null pointers. It is wrapped in a try so I think we're ok
      if(partId == myPart.getId()) {
        editingPartIndex = countingPartIndex;
        break;
      }
      countingPartIndex++;
    }
    return editingPartIndex;
  }
}
