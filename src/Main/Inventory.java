package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public Part lookupPart(int partId) {
        return allParts.stream().filter(part -> part.getId() == partId).findFirst().orElse(null);
    }



}
