package Elements;
import Main.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class PartsTable {
    private final int defaultPadding = 10;

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
}
