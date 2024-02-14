package stock.business;

public enum PlaceOfProduct {
    InStore(1),
    InStock(2);
    private final int value;


    private PlaceOfProduct(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static PlaceOfProduct fromInteger(int x) {
        switch(x) {
        case 1:
            return InStore;
        case 2:
            return InStock;
        }
        return null;
    }

    public static PlaceOfProduct fromString(String x) {
        switch(x) {
        case "store":
            return InStore;
        case "stock":
            return InStock;
        }
        return null;
    }

}
