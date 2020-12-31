package Main;

/**
 * InHouse class that extends the Part class
 * InHouse is a type of part manufactured "InHouse" and requires a machine id
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/31/2020
 */
public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * getter for machineid
     * @return machine id for the part
     */
    public int getMachineId () {
        return machineId;
    }

    /**
     * setter for machineId
     * @param machineId - the machineId
     */
    public void setMachineId (int machineId) {
        this.machineId = machineId;
    }
}
