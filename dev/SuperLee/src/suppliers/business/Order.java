package suppliers.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static suppliers.business.Utils.*;

public class Order{
    private int orderId;
    private transient Map<String,Map<Integer,Integer>> supplierTocatalogueNumToAmount;
    private transient Map<String, String> contactsForSuppliers;
    private transient Map<String, Map<Integer,Double>> suppliersProductPriceMap;
    private transient Map<String, Map<Integer,Double>> suppliersProductPriceBeforeDiscountMap;
    private transient Map<String, Boolean> arriveMap;
    private Date orderDate;
    private int branchId;

    public Order(int orderId, Map<String,Map<Integer,Integer>> supplierTocatalogueNumToAmount, Map<String, Map<Integer,Double>> suppliersProductPriceMap, Map<String, Map<Integer,Double>> suppliersProductPriceBeforeDiscountMap,int branchId)
    {
        this.orderId = orderId;
        this.supplierTocatalogueNumToAmount = supplierTocatalogueNumToAmount;
        this.orderDate = new Date(System.currentTimeMillis());
        this.contactsForSuppliers = new HashMap<>();
        this.suppliersProductPriceMap = suppliersProductPriceMap;
        this.suppliersProductPriceBeforeDiscountMap = suppliersProductPriceBeforeDiscountMap;
        this.branchId = branchId;
        this.arriveMap = new HashMap<>();
        for(String supplierId : suppliersProductPriceMap.keySet()){
            arriveMap.put(supplierId,false);
        }
    }

    public Order(int orderId, Map<String,Map<Integer,Integer>> supplierTocatalogueNumToAmount, Date orderDate, Map<String, Map<Integer,Double>> suppliersProductPriceMap, Map<String, Map<Integer,Double>> suppliersProductPriceBeforeDiscountMap, int branchId)
    {
        this.orderId = orderId;
        this.supplierTocatalogueNumToAmount = supplierTocatalogueNumToAmount;
        this.orderDate = orderDate;
        this.contactsForSuppliers = new HashMap<>();
        this.suppliersProductPriceMap = suppliersProductPriceMap;
        this.suppliersProductPriceBeforeDiscountMap = suppliersProductPriceBeforeDiscountMap;
        this.branchId = branchId;
        this.arriveMap = new HashMap<>();
        for(String supplierId : suppliersProductPriceMap.keySet()){
            arriveMap.put(supplierId,false);
        }
    }

    public Map<String, String> getContactsForSuppliers() {
        return contactsForSuppliers;
    }

    public Map<String, Map<Integer, Integer>> getProductsMap() {
        return supplierTocatalogueNumToAmount;
    }

    public void setProductsMap(Map<String, Map<Integer, Integer>> supplierTocatalogueNumToAmount) {
        for(Map.Entry<String,Map<Integer,Integer>> entry : supplierTocatalogueNumToAmount.entrySet()){
            this.supplierTocatalogueNumToAmount.put(entry.getKey(), new HashMap<>());
            for(Map.Entry<Integer,Integer> catalogueAmount : entry.getValue().entrySet()){
                this.supplierTocatalogueNumToAmount.get(entry.getKey()).put(catalogueAmount.getKey(),catalogueAmount.getValue());
            }
        }
    }

    public Map<Integer,Double> getPriceMapForSupplier(String companyId) {
        return suppliersProductPriceMap.get(companyId);
    }

    public void makeArrive(String supplierId){
        arriveMap.put(supplierId,true);
    }

    public boolean getArrive(String supplierId){
        return arriveMap.get(supplierId);
    }

    public double getPriceForSupplier(String companyId) {
        double sum = 0;
        for(int catNum : suppliersProductPriceMap.get(companyId).keySet()){
            sum += suppliersProductPriceMap.get(companyId).get(catNum);
        }
        return sum;
    }

    public double getPriceForSupplierBeforeDiscount(String companyId) {
        double sum = 0;
        for(int catNum : suppliersProductPriceBeforeDiscountMap.get(companyId).keySet()){
            sum += suppliersProductPriceBeforeDiscountMap.get(companyId).get(catNum);
        }
        return sum;
    }

    public double getPriceTotal(){
        double sum = 0;
        for(String companyId : suppliersProductPriceMap.keySet()){
            for(double price : suppliersProductPriceMap.get(companyId).values()){
                sum += price;
            }
        }
        return sum;
    }

    public Map<Integer,Double> getPriceForSupplierMapBeforeDiscount(String companyId) {
        return suppliersProductPriceBeforeDiscountMap.get(companyId);
    }

    public double getPriceTotalBeforeDiscount(){
        double sum = 0;
        for(String companyId : suppliersProductPriceBeforeDiscountMap.keySet()){
            for(double price : suppliersProductPriceBeforeDiscountMap.get(companyId).values()){
                sum += price;
            }
        }
        return sum;
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setContactForSupplier(String companyId, String contactId){
        if(!ordersToSupplier(companyId)){
            throw new IllegalArgumentException("This order doesn't include any products from supplier with ID " + companyId);
        }
        contactsForSuppliers.put(companyId, contactId);
    }
    public boolean ordersToSupplier(String supplierId){
        return supplierTocatalogueNumToAmount.containsKey(supplierId);
    }

    public void removeSupplierContact(String supplierId)
    {
        contactsForSuppliers.remove(supplierId);
    }

    public void removeContact(String contactId)
    {
        for(String supplierId : contactsForSuppliers.keySet())
            if(contactsForSuppliers.get(supplierId) == contactId)
                contactsForSuppliers.remove(supplierId);
    }

    public int getBranchId(){
        return branchId;
    }
}
