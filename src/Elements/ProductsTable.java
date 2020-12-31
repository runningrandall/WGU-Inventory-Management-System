package Elements;

import Main.Inventory;
import Main.Part;
import Main.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.Optional;

/**
 * ProductsTable class implements the UI components and logic that
 * allow products to be added, modified, and deleted
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/31/2020
 */
public class ProductsTable {
  private final Inventory inventory;
  private final TableView<Product> productsTable;
  private final int defaultPadding = 10;
  // create a alert
  private final Alert a = new Alert(Alert.AlertType.NONE);
  private final String costRegex = "^[0-9]+.[0-9]{2}$";

  /**
   * ProductsTable constructor
   * @param inventory - the current inventory
   * @param productsTable - productsTable used in the main view
   */
  public ProductsTable(Inventory inventory, TableView<Product> productsTable) {
    this.inventory = inventory;
    this.productsTable = productsTable;
  }

  /**
   * method to get the header UI elements for the products header
   * @param productsTable - productsTable from the main view
   * @param inventory - the current inventory
   * @return HBox
   */
  public HBox getProductsHeader(TableView<Product> productsTable, Inventory inventory) {

    Label productsTableLabel = new Label("Products");

    TextField productsTableSearch = new TextField(); // search field
    productsTableSearch.setPromptText("Search by Product ID or Name"); // placehoder
    productsTableSearch.addEventHandler(KeyEvent.KEY_RELEASED, event -> { // event handler
      String searchText = productsTableSearch.getText();
      if (searchText.length() == 0) {
        productsTable.setItems(inventory.getAllProducts());
      }
      try {
        int productId = Integer.parseInt(searchText);
        productsTable.setItems(FXCollections.observableArrayList(inventory.lookupProduct(productId)));
      } catch (Exception e) {
        productsTable.setItems(inventory.lookupProduct(searchText));
      }
    });

    HBox productsHeader = new HBox(defaultPadding * 5, productsTableLabel, productsTableSearch);
    productsHeader.setAlignment(Pos.BASELINE_LEFT); // alignment
    HBox.setMargin(productsTableSearch, new Insets(0, 0, 0, 100)); // move it to the right

    return productsHeader;
  }

  /**
   * method to get the footer ui elements for the products footer
   * @return HBox
   */
  public HBox getProductsFooter() {
    // add button
    Button productAddBtn = new Button("Add");
    productAddBtn.setOnAction(actionEvent -> showProductForm(null));

    // modify button
    Button productModifyBtn = new Button("Modify");
    productModifyBtn.setOnAction(actionEvent -> {
      Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
      if (selectedProduct == null) {
        showError("Please select a product to modify.");
      } else {
        showProductForm(selectedProduct.getId());
      }
    });

    // delete button
    Button productDeleteBtn = new Button("Delete");
    productDeleteBtn.setOnAction(actionEvent -> {
      Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
      if (selectedProduct == null) {
        showError("Please select a product to delete.");
      } else if (selectedProduct.getAllAssociatedParts().size() > 0) {
        showError("You cannot delete a product that has associated parts");
      } else {
        // must confirm the delete via confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete a Product");
        alert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();
        // if the alert is confirmed then delete the part, otherwise close the alert
        if (result.isPresent() && result.get() == ButtonType.OK){
          if(!inventory.deleteProduct(selectedProduct)) {
            showError("There was an error deleting the product. Please try again later.");
          }
        } else {
          alert.close();
        }
      }
    });

    HBox productsFooter = new HBox(defaultPadding, productAddBtn, productModifyBtn, productDeleteBtn);
    productsFooter.setAlignment(Pos.BASELINE_RIGHT); // alignment
    return productsFooter;
  }

  /**
   * method to show the product add/modify dialog form
   * @param productId - the product id we are editing (can be null)
   */
  private void showProductForm(Integer productId) {
    // determine if we are editing or adding a new part
    boolean isEditing = productId != null;

    // if editing grab the part or create a simple one
    Product product = isEditing
      ? inventory.lookupProduct(productId)
      : new Product(0, "temp", 0.00, 0, 0, 0);

    // create the text fields
    TextField productIdTf = new TextField();
    TextField productNameTf = new TextField();
    TextField productInvTf = new TextField();
    TextField productCostTf = new TextField();
    TextField productMaxTf = new TextField();
    TextField productMinTf = new TextField();
    ObservableList<Part> associatedParts = product.getAllAssociatedParts();
    // we need a backup here as well in case the user cancels
    ObservableList<Part> backupAssociatedParts = FXCollections.observableArrayList(associatedParts);

    // populate the form if editing
    if (isEditing) {
      try {
        productIdTf.setText(Integer.toString(product.getId()));
        productNameTf.setText(product.getName());
        productInvTf.setText(Integer.toString(product.getStock()));
        productCostTf.setText(Double.toString(product.getPrice()));
        productMaxTf.setText(Integer.toString(product.getMax()));
        productMinTf.setText(Integer.toString(product.getMin()));
      } catch (Exception e) {
        showError(e.getMessage());
      }
    }

    // setup parts tables
    TableView<Part> associatedPartsTable = getPartsTable();
    associatedPartsTable.setItems(associatedParts);
    TableView<Part> allPartsTable = getPartsTable();
    allPartsTable.setItems(inventory.getAllParts());
    associatedPartsTable.setPlaceholder(new Label("No parts associated yet with this product."));
    allPartsTable.setPlaceholder(new Label("No parts found."));

    // add part button
    Button addPartToProductButton = new Button("Add Associated Part");
    addPartToProductButton.setOnAction(actionEvent -> {
      Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
      if (selectedPart == null) {
        showError("Please select a part to add.");
      } else {
        product.addAssociatedPart(selectedPart);
      }
    });

    // remove part button
    Button removePartFromProductButton = new Button("Remove Associated Part");
    removePartFromProductButton.setOnAction(actionEvent -> {
      Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
      if (selectedPart == null) {
        showError("Please select a part to remove.");
      } else {
        // must confirm the delete via confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Remove a Part");
        alert.setContentText("Are you sure you want to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        // if the alert is confirmed then delete the part, otherwise close the alert
        if (result.isPresent() && result.get() == ButtonType.OK){
          if(!product.deleteAssociatedPart(selectedPart)) {
            alert.close();
            showError("There was an error deleting the part. Please try again later.");
          }
        } else {
          alert.close();
        }
      }
    });

    // setup the dialog
    Dialog<String> addProductDialog = new Dialog<>();
    addProductDialog.setTitle(isEditing ? "Modify Product" : "Add Product");
    addProductDialog.setResizable(true);

    // product id
    productIdTf.setPromptText("Auto Gen - Disabled");
    productIdTf.setDisable(true); // WHY SHOW IT IF ALWAYS DISABLED?
    Label productIdLabel = new Label("ID");

    // product name
    productNameTf.setPromptText("Product Name");
    Label productNameLabel = new Label("Name");

    // product inventory
    productInvTf.setPromptText("0");
    Label productInventoryLabel = new Label("Inventory");
    // force the field to be numeric only
    productInvTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        productInvTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // product cost
    productCostTf.setPromptText("0.00");
    Label productCostLabel = new Label("Cost");
    // force to be double entry only
    productCostTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches(costRegex)){
        productCostTf.setText(newV.replaceAll("[" + costRegex + "]", ""));
      }
    });

    // product max
    productMaxTf.setPromptText("10");
    Label productMaxLabel = new Label("Max");
    // numbers only
    productMaxTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        productMaxTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // product min
    productMinTf.setPromptText("1");
    Label productMinLabel = new Label("Min");
    // numbers only
    productMinTf.textProperty().addListener((observableValue, oldV, newV) -> {
      if (!newV.matches("\\d*")) {
        productMinTf.setText(newV.replaceAll("[^\\d]", ""));
      }
    });

    // setup the grid panes
    // this is a nested gridpane structure
    GridPane gridPaneParent = new GridPane();
    GridPane formGridPane = new GridPane();
    GridPane tableViewGridPane = new GridPane();
    ColumnConstraints constraints = new ColumnConstraints();
    constraints.setHgrow(Priority.ALWAYS);
    formGridPane.getColumnConstraints().addAll(new ColumnConstraints(), constraints);
    gridPaneParent.getColumnConstraints().addAll(new ColumnConstraints(), constraints);
    tableViewGridPane.getColumnConstraints().addAll(new ColumnConstraints(), constraints);

    formGridPane.setVgap(10);
    formGridPane.setHgap(10);
    formGridPane.setPadding(new Insets(defaultPadding * 3));
    tableViewGridPane.setVgap(10);
    tableViewGridPane.setHgap(10);
    tableViewGridPane.setPadding(new Insets(defaultPadding * 3));
    // add basic text fields
    formGridPane.add(productIdLabel, 0, 0);
    formGridPane.add(productIdTf, 1, 0);
    formGridPane.add(productNameLabel, 0, 1);
    formGridPane.add(productNameTf, 1, 1);
    formGridPane.add(productInventoryLabel, 0, 2);
    formGridPane.add(productInvTf, 1, 2);
    formGridPane.add(productCostLabel, 0, 3);
    formGridPane.add(productCostTf, 1, 3);
    formGridPane.add(productMaxLabel, 0, 4);
    formGridPane.add(productMaxTf, 1, 4);
    formGridPane.add(productMinTf, 2, 4);
    formGridPane.add(productMinLabel, 3, 4);

    // right col table view
    tableViewGridPane.add(getAllPartsView(allPartsTable), 0, 0);
    tableViewGridPane.add(allPartsTable, 0, 1);
    tableViewGridPane.add(addPartToProductButton, 0, 2);
    tableViewGridPane.add(associatedPartsTable, 0, 3);
    tableViewGridPane.add(removePartFromProductButton, 0, 4);

    // setup parent 2 col view
    gridPaneParent.add(formGridPane, 0, 0);
    gridPaneParent.add(tableViewGridPane, 1, 0);

    // set content of the dialog
    addProductDialog.getDialogPane().setContent(gridPaneParent);

    // add dialog buttons
    addProductDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

    // add button event listeners
    final Button saveButton = (Button) addProductDialog.getDialogPane().lookupButton(ButtonType.OK);
    final Button cancelButton = (Button) addProductDialog.getDialogPane().lookupButton(ButtonType.CANCEL);

    // process a cancel/close
    cancelButton.addEventFilter(
      ActionEvent.ACTION,
      event -> {
        associatedParts.clear();
        backupAssociatedParts.forEach(product::addAssociatedPart);
      }
    );

    // process the save
    saveButton.addEventFilter(
      ActionEvent.ACTION,
      event -> {
        try {
          String productName = productNameTf.getText();
          boolean productNameValid = productName.length() > 0;
          int productInventory = Integer.parseInt(productInvTf.getText());
          double productCost = Double.parseDouble(productCostTf.getText());
          boolean productCostValid = productCost >= 0;
          int productMax = Integer.parseInt(productMaxTf.getText());
          boolean productMaxValid = productMax >= 0;
          int productMin = Integer.parseInt(productMinTf.getText());
          boolean productMinValid = productMin >= 0;
          boolean productMinMaxValid = productMaxValid && productMinValid && productMax >= productMin;
          boolean productInventoryValid = productInventory <= productMax && productInventory >= productMin;

          // if we passed validation
          if (!productNameValid) {
            showError("Please provide a valid product name");
            event.consume();
          } else if (!productInventoryValid) {
            showError("Please provide a valid product inventory. It must be between min and max");
            event.consume();
          } else if (!productCostValid) {
            showError("Pleae provide a valid cost");
            event.consume();
          } else if (!productMinMaxValid) {
            showError("Please provide valid min/max values");
            event.consume();
          } else {
            Product newProduct = new Product(isEditing ? productId : getNewProductId(), productName, productCost, productInventory, productMin, productMax);
            // now copy all the associated parts
            for(Part myPart : associatedParts) {
              newProduct.addAssociatedPart(myPart);
            }
            if (isEditing) {
              int productIndex = getPartIndexFromPartId(productId);
              inventory.updateProduct(productIndex, newProduct);
            } else {
              inventory.addProduct(newProduct);
            }
            addProductDialog.close();
          }
        } catch (Exception e) {
          showError(e.getMessage());
          event.consume();
        }
      });
    addProductDialog.showAndWait();
    // reset when closing
    addProductDialog.close();
    addProductDialog.hide();
  }

  /**
   * method to bet the all parts table view which includes the table and search box
   * @param partsTable - the table of all parts
   * @return HBox
   */
  public HBox getAllPartsView(TableView<Part> partsTable) {
    // setup the parts table
    Label partsTableLabel = new Label("All Parts"); // label
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
   * Method to build a skeleton parts table
   * @return TableView
   */
  private TableView<Part> getPartsTable (){
    TableView<Part> associatedPartsTable = new TableView<>();
    //Creating columns
    TableColumn partIdCol = new TableColumn("Part ID");
    partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    TableColumn partNameCol = new TableColumn("Part Name");
    partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    TableColumn partStockCol = new TableColumn("Inventory Level");
    partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    TableColumn partPriceCol = new TableColumn("Price/Cost Per Item");
    partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    associatedPartsTable.getColumns().addAll(partIdCol, partNameCol, partStockCol, partPriceCol);
    associatedPartsTable.setMaxSize(350, 200);
    return associatedPartsTable;
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
   * method to get the newest productId
   * @return partId
   */
  private int getNewProductId() {
    int productId = 0;
    ObservableList<Product> allProducts = inventory.getAllProducts();
    for(Product myProduct : allProducts) {
      if(productId <= myProduct.getId()) {
        productId = myProduct.getId() + 1;
      }
    }
    return productId;
  }

  /**
   * method to get the product index from a product id
   * it might be better for this method to be in the Inventory class
   * but the UML diagram said not to
   * @param productId - the product id we are looking for
   * @return productIndex
   */
  private int getPartIndexFromPartId(int productId) {
    int editingProductIndex = 0;
    int countingProductIndex = 0;
    ObservableList<Product> allProducts = inventory.getAllProducts();
    for(Product myProduct : allProducts) {
      // I'm worried about null pointers. It is wrapped in a try so I think we're ok
      if(productId == myProduct.getId()) {
        editingProductIndex = countingProductIndex;
        break;
      }
      countingProductIndex++;
    }
    return editingProductIndex;
  }
}
