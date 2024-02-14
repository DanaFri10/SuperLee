package stock.business;

import java.time.LocalDate;

public class BranchInstance {
    private int instanceId;
    private int productId; 
    private double boughtPrice;
    private LocalDate expireDate;
    private String defectedDescription;
    private PlaceOfProduct placeOfProduct;
    private LocalDate arriveDate;

    public BranchInstance(int _instanceId, int _productId, double _boughtPrice,LocalDate _expireDate, PlaceOfProduct _placeOfProduct, LocalDate _arriveDate){
        instanceId = _instanceId;
        productId = _productId;
        expireDate = _expireDate;
        boughtPrice = _boughtPrice;
        defectedDescription = null;
        placeOfProduct = _placeOfProduct;
        arriveDate =_arriveDate;
    }

    public void setDefected(String description){
        defectedDescription = description;
    }

    public int getProductId(){
        return productId;
    }

    public boolean isExpired(){
        if(expireDate == null){
            return true;
        }
        return LocalDate.now().isAfter(expireDate);
    }

    public int getInstanceId(){
        return instanceId;
    }

    public String getdescription(){
        return defectedDescription;
    }

    public PlaceOfProduct getPlaceOfProduct(){
        return placeOfProduct;
    }

    public double getBoughtPrice() {
        return boughtPrice;
    }

    public int daysExpiered(){
        return LocalDate.now().compareTo(expireDate);
    }

    public LocalDate getArriveDate() {
        return arriveDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void changePlaceOfProduct(String _placeOfProduct) {
        this.placeOfProduct = PlaceOfProduct.fromString(_placeOfProduct);
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }
}
