package shared.GUI.warehouseWorker.model;

public class UniversalProductModel {
    private int id;
    private String name;
    private String manufacturer;

    public UniversalProductModel(int id, String name, String manufacturer) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String toString() {
        return name + ", manufactured by " + manufacturer + "(ID: " + id + ")";
    }
}
