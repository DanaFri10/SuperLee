package suppliers.business;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class PeriodicOrderAgreement {
    private Map<Integer,Integer> productAmounts;
    private boolean[] orderDays;
    private String assignedContactId;

    public PeriodicOrderAgreement(Map<Integer,Integer> productAmounts){
        this.productAmounts = productAmounts;
        this.orderDays = new boolean[7];
    }

    public void removeProduct(int catalogueNum){
        if(productAmounts.containsKey(catalogueNum)){
            productAmounts.remove(catalogueNum);
        }
    }
    public Map<Integer, Integer> getProductAmounts() {
        return productAmounts;
    }

    public void setProductAmounts(Map<Integer, Integer> productAmounts) {
        this.productAmounts = productAmounts;
    }

    public boolean[] getOrderDays() {
        return orderDays;
    }

    public void setOrderDays(boolean[] orderDays) {
        this.orderDays = orderDays;
    }

    public String getAssignedContactId() {
        return assignedContactId;
    }

    public boolean isDueToday(){
        if(orderDays == null){
            return false;
        }
        Calendar c = Calendar.getInstance();
        return orderDays[c.get(Calendar.DAY_OF_WEEK)-1];
    }

    public boolean canUpdate(){
        Calendar c = Calendar.getInstance();
        return canUpdate(c);
    }

    public boolean canUpdate(Calendar c){
        if(orderDays == null){
            return false;
        }
        return !orderDays[c.get(Calendar.DAY_OF_WEEK) % 7];
    }

    public void setAssignedContactId(String assignedContactId) {
        this.assignedContactId = assignedContactId;
    }
}
