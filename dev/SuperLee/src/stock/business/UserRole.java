package stock.business;

public enum UserRole {
    StoreManager(1),
    Cashier(2),
    WareHoushWorker(3),
    SupplierManager(4);

    private final int value;
    private UserRole(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static UserRole fromInteger(int x) {
        switch(x) {
        case 1:
            return StoreManager;
        case 2:
            return Cashier;
        case 3:
            return WareHoushWorker;
        case 4:
            return SupplierManager;
        }
        return null;
    }
    
    public static UserRole fromString(String x) {
        switch(x) {
        case "1":
            return StoreManager;
        case "2":
            return Cashier;
        case "3":
            return WareHoushWorker;
        case "4":
            return SupplierManager;
        }
        return null;
    }
}
