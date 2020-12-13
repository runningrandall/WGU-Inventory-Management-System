package Main;

/**
 *
 * @author Randall Adams
 */
public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * getter for machineid
     * @return machineId
     */
    public int getMachineId () {
        return machineId;
    }

    /**
     * setter for machineId
     * @param machineId
     */
    public void setMachineId (int machineId) {
        this.machineId = machineId;
    }
}
