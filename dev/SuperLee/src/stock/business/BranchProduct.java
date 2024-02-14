package stock.business;

import java.time.LocalDate;
import java.util.ArrayList;


public class BranchProduct {
    private int id;
    private double priceSold;
    private int demand;
    private int minimum;
    private String placeInStore;
    private String path;
    private transient BranchDiscount mainDiscount;

    public BranchProduct(int _id, double originalPriceSold, int _demand, String _placeInStore, int _minimum, String _path){
        id = _id;
        priceSold = originalPriceSold;
        demand = _demand;
        placeInStore = _placeInStore;
        minimum = _minimum;
        path = _path;
        mainDiscount = null;
    }


    public double getcurrentPriceSold(ArrayList<BranchDiscount> sellingDiscounts){
        double highest = 0;
        //check if main discount is expiered
        if(mainDiscount != null && LocalDate.now().isBefore(mainDiscount.getExpiredDate()) && mainDiscount.getBeginningDate().isBefore(LocalDate.now()) ){
            return priceSold * (1 - (mainDiscount).getDiscountNumericValue()); 
        }

        for(BranchDiscount discount : sellingDiscounts){
            //checks idf the discount is expierd
            if(LocalDate.now().isBefore(discount.getExpiredDate()) & discount.getBeginningDate().isBefore(LocalDate.now())){
                if(discount.getDiscountNumericValue() > highest){
                    highest = discount.getDiscountNumericValue();
                }
            }
        }
        return priceSold * (1 - highest);
    }

    // added getters and setters :
    public int getId() {
        return id;
    }

    public double getPriceSold() {
        return priceSold;
    }



    public void setDemand(int _demand){
        demand = _demand;
    }


    public String getplaceInStore() {
        return placeInStore;
    }

    public String getPath(){
        return path;
    }

    public int getMinimum() {
        return minimum;
    }

    public BranchDiscount getDiscount(){
        return mainDiscount;
    }
    public void setMainDiscount(BranchDiscount mainDiscount) {
        this.mainDiscount = mainDiscount;
    }


    public int getDemand() {
        return demand;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setPlaceInStore(String placeInStore) {
        this.placeInStore = placeInStore;
    }
    public void setPriceSold(double priceSold) {
        this.priceSold = priceSold;
    }


    public void setInfo(Double sellingPrice, String path2, int demand2, String placeInStore2, int minimum2) {
        priceSold = sellingPrice;
        path = path2;
        demand = demand2;
        placeInStore = placeInStore2;
        minimum = minimum2;
    }
}
