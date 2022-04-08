package model;

/**Inherits Part class*/
public class InHouse extends Part {
    private int machineId;

    /**InHouse Constructor*/
    public InHouse(int id, String name, int stock, double price, int min, int max, int machineId) {

        super(id, name, stock, price, min, max);
        this.machineId = machineId;

    }
    /**InHouse MachineId setter*/
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**InHouse MachineId getter*/
    public int getMachineId(){
        return machineId;
    }
}



