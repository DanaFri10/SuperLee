package stock.business;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import stock.dal.BranchCategoryDiscountDAO;
import stock.dal.BranchProductInstanceDAO;
import stock.dal.BranchProductDAO;

public class Branch {
    private int branchId;
    private LocalDate startTime;
    private int timeStep;
    private BranchCategoryDiscountDAO bdDAO;
    private BranchProductInstanceDAO biDAO;
    private BranchProductDAO bpDAO;

    public Branch(int _branchId, LocalDate startTime, int timeStep) throws SQLException{
        branchId = _branchId;
        this.startTime = startTime;
        this.timeStep = timeStep;
        bdDAO = new BranchCategoryDiscountDAO(_branchId);
        biDAO = new BranchProductInstanceDAO(_branchId);
        bpDAO = new BranchProductDAO(_branchId);
    }


    public int getBranchId(){
        return branchId;
    }

    public void insertBranchProduct(BranchProduct product) throws SQLException {
        bpDAO.insertProduct(product);
    }

    public void insertDiscount(String path, BranchDiscount dicount) throws SQLException {
        bdDAO.insertDiscount(path, dicount);
    }

    public BranchProduct getProduct(int productId) throws SQLException {
        return bpDAO.getProduct(productId);
    }

    public void updateProduct(BranchProduct product) throws SQLException {
        bpDAO.updateProduct(product);
    }

    public void insertInstance(BranchInstance instance) throws SQLException {
        biDAO.insertInstance(instance);
    }

    public BranchInstance getInstance(int defetiveId) throws SQLException {
        return biDAO.getInstance(defetiveId);
    }

    public void updateInstance(BranchInstance instance) throws SQLException {
        biDAO.updateInstance(instance);
    }

    public void removeInstance(int insatnceId) throws SQLException {
        BranchInstance p = biDAO.getInstance(insatnceId);
        if(p == null)
            throw new IllegalArgumentException("This instance does not exist.");
        biDAO.removeInstance(insatnceId);
    }
    public void removeProduct(int productId) throws SQLException {
        bpDAO.removeProduct(productId);
        biDAO.removeInstanceOfProduct(productId);
    }

    public void setTimeStep(int _timeStep) {
        timeStep = _timeStep;
    }

    public ArrayList<BranchInstance> getExpired() throws SQLException {
        ArrayList<BranchInstance> returnExpireds = new ArrayList<>();
        ArrayList<BranchInstance> all = biDAO.getAll();
        for(BranchInstance instance : all){
            if(instance.isExpired()){
                returnExpireds.add(instance);
            }
        }
        return returnExpireds;
    }
    
    public ArrayList<BranchInstance> getDefectives() throws SQLException {
        ArrayList<BranchInstance> returnDefectives = new ArrayList<>();
        ArrayList<BranchInstance> all = biDAO.getAll();
        for(BranchInstance instance : all){
            if(instance.getdescription() != null){
                returnDefectives.add(instance);
            }
        }
        return returnDefectives;
    }

    public Integer getTimeStep() {
        return timeStep;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate currTime){
        startTime = currTime;
    }


    public int getAmountInStore(int productId) throws SQLException {
        ArrayList<BranchInstance> instances = biDAO.getAll();
        int counter = 0;
        for(BranchInstance instance : instances){
            if(instance.getProductId() == productId & instance.getPlaceOfProduct().equals(PlaceOfProduct.InStore)){
                counter++;
            }
        }
        return counter;
    }

    public int getAmountInStock(int productId) throws SQLException {
        ArrayList<BranchInstance> instances = biDAO.getAll();
        int counter = 0;
        for(BranchInstance instance : instances){
            if(instance.getProductId() == productId & instance.getPlaceOfProduct().equals(PlaceOfProduct.InStock)){
                counter++;
            }
        }
        return counter;
    }

    public ArrayList<BranchProduct> getProductOnlyIn(String path) throws SQLException{
        ArrayList<BranchProduct> allBranchProducts = bpDAO.getAll();
        ArrayList<BranchProduct> returnBranchProducts = new ArrayList<>();
        for(BranchProduct bp : allBranchProducts){
            if(bp.getPath().equals(path)){
                returnBranchProducts.add(bp);
            }
        }
        return returnBranchProducts;
    }

    public ArrayList<String> getImmidiateSubCategories(String path) throws SQLException{
        ArrayList<BranchProduct> allBranchProducts = bpDAO.getAll();
        Set<String> returnCategoriess = new HashSet<>();
        for(BranchProduct bp : allBranchProducts){
            if(bp.getPath().startsWith(path)){
                String crop = bp.getPath().replaceFirst(path, "");
                if(!crop.isEmpty()) {
                    if(!path.isEmpty()) {
                        crop = crop.substring(1);
                    }
                    if (crop.contains("/")) {
                        returnCategoriess.add(crop.substring(0, crop.indexOf('/')));
                    }else{
                        returnCategoriess.add(crop);
                    }
                }
            }
        }
        ArrayList<String> arrayListCategories = new ArrayList<>();
        arrayListCategories.addAll(returnCategoriess);
        return arrayListCategories;
    }

    public double getFinalBuyingPrice(Integer productId) throws SQLException{
        ArrayList<BranchInstance> instances = biDAO.getAll();
        double sum = 0;
        int counter = 0;
        for(BranchInstance instance : instances){
            if(productId == instance.getProductId()){
                counter++;
                sum = sum + instance.getBoughtPrice();
            }
        }
        return (int)(Math.round((sum/counter)*1000))/1000.0;
    }

    public double getFinalSellingPrice(Integer productId) throws SQLException{
        BranchProduct bp = bpDAO.getProduct(productId);
        ArrayList<BranchDiscount> bds = bdDAO.getAllPath(bp.getPath());

        return (int)(Math.round((bp.getcurrentPriceSold(bds))*1000))/1000.0;
    }

    public Map<Integer,Integer> getShortage() throws SQLException{
        ArrayList<BranchProduct> allBranchProducts = bpDAO.getAll();
        Map<Integer,Integer> returnProducts = new HashMap<>();
        for(BranchProduct bp : allBranchProducts){
            int min = bp.getMinimum();
            int amount = getAmountInStock(bp.getId()) + getAmountInStore(bp.getId());
            if(amount < min){
                returnProducts.put(bp.getId(),min-amount);
            }
        }
        return returnProducts;
    }

    public ArrayList<BranchProduct> getProductInAndInSubs(String path) throws SQLException{
        ArrayList<BranchProduct> allBranchProducts = bpDAO.getAll();
        ArrayList<BranchProduct> returnBranchProducts = new ArrayList<>();
        for(BranchProduct bp : allBranchProducts){
            if(bp.getPath().startsWith(path + "/") | bp.getPath().equals(path)){
                returnBranchProducts.add(bp);
            }
        }
        return returnBranchProducts;
    }

    public void changeLocationOfInstance(int instanceId, String place) throws SQLException{
        BranchInstance instance = biDAO.getInstance(instanceId);
        if(instance == null)
            throw new IllegalArgumentException("There is no instance with this ID");
        instance.changePlaceOfProduct(place);
        biDAO.updateInstance(instance);
    }

    public void InsertAmountInstances(int amount, double boughtPrice, int productId, LocalDate arriveDate) throws SQLException {
        int highest = 0;
        ArrayList<BranchInstance> instances = biDAO.getAll();
        for(BranchInstance instance : instances){
            if(instance.getInstanceId() > highest){
                highest = instance.getInstanceId();
            }
        }
        for(int i=0; i < amount; i++){
            highest++;
            BranchInstance instance = new BranchInstance(highest, productId, boughtPrice,null , PlaceOfProduct.InStock, arriveDate);
            biDAO.insertInstance(instance);

        }
    }

    public ArrayList<BranchProduct> getAllProducts() throws SQLException {
        return bpDAO.getAll();
    }

    //for tests
    public void deleteAllDiscounts() throws SQLException {
        bdDAO.DeleteAll();
    }
}
