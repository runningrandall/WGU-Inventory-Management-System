package Elements;

import Main.Inventory;
import Main.Product;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class ProductsTable {
    private final int defaultPadding = 10;

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
}
