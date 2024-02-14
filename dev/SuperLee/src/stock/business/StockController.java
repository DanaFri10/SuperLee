package stock.business;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.gson.GsonBuilder;
import shared.LocalDateAdapter;
import stock.dal.BranchDAO;
import stock.dal.ProductDAO;
import stock.dal.UserDAO;
import suppliers.business.SuppliersController;

public class StockController {
    private UserDAO uDAO;
    private BranchDAO bDAO;
    private ProductDAO pDao;
    private SuppliersController suppliersController;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();




    public StockController() throws SQLException {
            this.uDAO = new UserDAO();
            this.bDAO = new BranchDAO();
            this.pDao = new ProductDAO();
    }

    public void setSuppliersController(SuppliersController suppliersController){
        this.suppliersController = suppliersController;
    }

    public BranchDAO getbDAO() {
        return bDAO;
    }

    public ProductDAO getpDao() {
        return pDao;
    }

    /*
        This piece of code was taken from the previous Class ManagerService
         */

    public String createCategoryReport(int branchId, ArrayList<String> ListStringCategories) throws Exception{
        int counter;
        Branch branch = bDAO.getBranch(branchId);


        String str = "\n";
        str = str + "Date: " + LocalDate.now().toString() + "\n";
        str = str + "branch: " + branchId + "\n";
        for(String path : ListStringCategories){
            counter = 1;
            str = str + "\n";
            str = str + "category name: " + path  + "\n";
            str = str + "products which belong to this category are: \n";
            ArrayList<BranchProduct> branchProducts = branch.getProductInAndInSubs(path);
            for(BranchProduct branchProduct :  branchProducts){

                Product product = pDao.getProduct(branchProduct.getId());
                str = str + product.getName();

                if(counter == branchProducts.size()){
                    str = str + "\n";
                }
                else if(counter%5 == 0){
                    str = str + ",\n";
                }
                else{
                    str = str + ", ";
                }
                counter++;
            }
        }
        return str;
    }

    public String createDefectivesReport(int branchId) throws Exception {
        Branch branch = bDAO.getBranch(branchId);

        int timeStep = branch.getTimeStep();
        if(branch.getTimeStep() == -1){
            return "";
        }
        if(branch.getStartTime().plusDays(timeStep).isAfter(LocalDate.now()))
        {
            return "";
        }
        branch.setStartTime(LocalDate.now());
        bDAO.updateBranch(branch);

        String str = "\n";
        str = str + "Date: " + LocalDate.now().toString() + "\n";
        str = str + "branch: " + branchId + "\n";
        str = str + "the following items are defected :" + "\n";
        for(BranchInstance defect : branch.getDefectives()){
            Product defectProduct  = pDao.getProduct(branchId);
            BranchProduct defectBranchProduct = branch.getProduct(defect.getProductId());
            // id, productName, place in store, description,
            str = str + " instance id: " + defect.getInstanceId() + ", product Name: " + defectProduct.getName() + ", category: " + defectBranchProduct.getPath() + ", description: " + defect.getdescription() + "\n";
        }
        str = str + "the folowing items are expired :" + "\n";
        for(BranchInstance expired : branch.getExpired()){
            Product expiredProduct = pDao.getProduct(expired.getProductId());
            BranchProduct expiredBranchProduct = branch.getProduct(expired.getProductId());
            // id, productName, place in store, daysexpired
            str = str  + " instance id: " + expired.getInstanceId() + " product Name: " + expiredProduct.getName() + " categories: " + expiredBranchProduct.getPath()+ " how many days expiered: " + expired.daysExpiered() +  "\n";
        }
        return str;
    }
    public String createDefectivesReportOnDemand(int branchId) throws Exception {
        Branch branch = bDAO.getBranch(branchId);

//        int timeStep = branch.getTimeStep();
//        if(branch.getTimeStep() == -1){
//            return "";
//        }
//        if(branch.getStartTime().plusDays(timeStep).isAfter(LocalDate.now()))
//        {
//            return "";
//        }
//        branch.setStartTime(LocalDate.now());
//        bDAO.updateBranch(branch);

        String str = "\n";
        str = str + "Date: " + LocalDate.now().toString() + "\n";
        str = str + "branch: " + branchId + "\n";
        str = str + "the following items are defected :" + "\n";
        for(BranchInstance defect : branch.getDefectives()){
            Product defectProduct = pDao.getProduct(branchId);
            BranchProduct defectBranchProduct = branch.getProduct(defect.getProductId());
            // id, productName, place in store, description,
            str = str + " instance id: " + defect.getInstanceId() + ", product Name: " + defectProduct.getName() + ", category: " + defectBranchProduct.getPath() + ", description: " + defect.getdescription() + "\n";
        }
        str = str + "the folowing items are expired :" + "\n";
        for(BranchInstance expired : branch.getExpired()){
            Product expiredProduct = pDao.getProduct(expired.getProductId());
            BranchProduct expiredBranchProduct = branch.getProduct(expired.getProductId());
            // id, productName, place in store, daysexpired
            str = str  + " instance id: " + expired.getInstanceId() + " product Name: " + expiredProduct.getName() + " categories: " + expiredBranchProduct.getPath()+ " how many days expiered: " + expired.daysExpiered() +  "\n";
        }
        return str;
    }



    public String getShortage(int branchId) throws SQLException{
        return gson.toJson(bDAO.getBranch(branchId).getShortage());
    }

    public int getAmountOfProduct(int branchId, int productId) throws SQLException {
        Branch branch = bDAO.getBranch(branchId);
        return branch.getAmountInStore(productId) + branch.getAmountInStock(productId);
    }

    public String createShortageReport(int branchId, Map<Integer,Integer> shortageMap) throws Exception {
        Branch branch = bDAO.getBranch(branchId);
        ArrayList<Integer> productIds = new ArrayList<>();
        String str = "\nimportant:\n";
        str = str + "Date: " + LocalDate.now().toString() + "\n";
        str = str + "branch: " + branchId + "\n";


        for(Integer productId : shortageMap.keySet()){
            BranchProduct bProduct= branch.getProduct(productId);
            Product product = pDao.getProduct(productId);
            int minimum = bProduct.getMinimum();
            int currentAmount = minimum - shortageMap.get(productId);
           str = str + "product id: " + productId + ", name: " + product.getName() + ", current amount: "+ currentAmount + ", minimum: "+ minimum + ".\n";
        }
        return str;
    }

    public String productReport(int branchId, int productId) throws Exception{
        Branch branch = bDAO.getBranch(branchId);
        BranchProduct branchProduct = branch.getProduct(productId);
        return "path: "+ branchProduct.getPath() +" demand: "+ branchProduct.getDemand() +" minimum: "+ branchProduct.getMinimum() +" price: "+ branchProduct.getPriceSold();
    }

    public String getAllProducts() throws SQLException {
        return gson.toJson(pDao.getAll());
    }


    public String createInventoryReport(int branchId) throws Exception {
        Branch branch = bDAO.getBranch(branchId);
        ArrayList<BranchProduct> branchProducts = branch.getAllProducts();
        String str = "\n";
        str = str + "Date: " + LocalDate.now().toString() + "\n";
        str = str + "branch: " + branchId + "\n";
        /*        Comparator<BranchProduct> comparator = new Comparator<BranchProduct>() {
            public int compare(BranchProduct bp1, BranchProduct bp2) {
                return pDao.getProduct(bp1.getId()).getName().compareTo(pDao.getProduct(bp2.getId()).getName());
            }
        }; */

        for(BranchProduct branchProduct : branchProducts){
            Product product = pDao.getProduct(branchProduct.getId());
            if(product == null){throw new Exception("product not exist\n");}
            str = str + "product name: " + product.getName() + ",";
            str = str + " place in store: " + branchProduct.getplaceInStore() + ",";
            str = str + " manufacturer: " + product.getManufacturer() + ",";
            str = str + " amount in stock: " + branch.getAmountInStock(branchProduct.getId()) + ",";
            str = str + " amount in store: " + branch.getAmountInStore(branchProduct.getId()) + ",";
            str = str + " original selling price: " + branchProduct.getPriceSold() + ",";
            str = str + " final selling price: " + branch.getFinalSellingPrice(branchProduct.getId()) + ",";
            str = str + " average buying price: " + branch.getFinalBuyingPrice(branchProduct.getId()) + ",";
            str = str + " demand: " + branchProduct.getDemand() + ",";
            str = str + " Can be supplied within: " + suppliersController.getEarliestSupplyTime(branchProduct.getId(),1) + "\n";
        }
        return str;
    }

    public String getBranchProductReport(int branchId, int productId) throws SQLException {
        Branch branch = bDAO.getBranch(branchId);
        BranchProduct branchProduct = branch.getProduct(productId);
        String str = "";
        Product product = pDao.getProduct(branchProduct.getId());
        if(product == null){throw new Error("product does not exist\n");}
        str = str + "Product name: " + product.getName() + "\n";
        str = str + "Place in store: " + branchProduct.getplaceInStore() + "\n";
        str = str + "Manufacturer: " + product.getManufacturer() + "\n";
        str = str + "Amount in store: " + branch.getAmountInStore(branchProduct.getId()) + "\n";
        str = str + "Amount in stock: " + branch.getAmountInStock(branchProduct.getId()) + "\n";
        str = str + "Final selling price: " + branch.getFinalSellingPrice(branchProduct.getId()) + "\n";
        str = str + "Average buying price: " + branch.getFinalBuyingPrice(branchProduct.getId()) + "\n";
        str = str + "Can be supplied within: " + suppliersController.getEarliestSupplyTime(productId,1) + "\n";
        return str;
    }

    //now finished
    public void createSellingDiscount(Integer branchId, String _precentage, String _categoryOrProduct, String startDate, String endDate, String NameOrid) throws Exception{
        Branch branch = bDAO.getBranch(branchId);
        Double percentage = checkIntegrityOfDouble(_precentage);
        LocalDate start = checkIntegrityOfDate(startDate);
        LocalDate end = checkIntegrityOfDate(endDate);
        if(!end.isAfter(start)){
            throw new Exception("End date must be after Start date");
        }
        if(percentage < 0 || percentage > 1){
            throw new Exception("Discount percentage must be between 0 and 1");
        }
        BranchDiscount dicount = new BranchDiscount(start, end, percentage);
        if (_categoryOrProduct.equals("1")){
            String path = NameOrid;
            branch.insertDiscount(path, dicount);
        }
        else if (_categoryOrProduct.equals("2")){
            int productId = checkIntegrityOfInt(NameOrid);
            BranchProduct branchProduct = branch.getProduct(productId);
            if(branchProduct == null){
                throw new Exception("product not found.");
            }
            branchProduct.setMainDiscount(dicount);
            branch.updateProduct(branchProduct);
        }
        else{ throw new Exception("not 1 or 2"); }
    }

//finished
    public void updateTimeStep(int branchId, String _timeStep) throws Exception {
        Branch branch = bDAO.getBranch(branchId);
        int timeStep = checkIntegrityOfInt(_timeStep);
        if(timeStep < 0){
            throw new Exception("Time step cannot be negative");
        }
        branch.setTimeStep(timeStep);
        bDAO.updateBranch(branch);
    }

    public void submitInstances(int branchId, String _insatnceIds, String _boughtPrice, String _productId, String expiredDate) throws Exception {
        Branch branch = bDAO.getBranch(branchId);
        if(branch == null)
            throw new IllegalArgumentException("This branch does not exist");

        Double boughtPrice = checkIntegrityOfDouble(_boughtPrice);
        if(boughtPrice < 0)
            throw new IllegalArgumentException("Bought price must be a positive number.");

        int productId = checkIntegrityOfInt(_productId);
        String[] insatnceIdsListString = _insatnceIds.split(" ");
        for(String insatnceIdString : insatnceIdsListString){
            checkIntegrityOfInt(insatnceIdString);
        }
        LocalDate expireDate;


        if(expiredDate.equals("-")){
            expireDate = checkIntegrityOfDate("9999-01-01");
        }
        else{
            expireDate = checkIntegrityOfDate(expiredDate);
        }

        BranchProduct p = branch.getProduct(productId);
        if(p == null){
            throw new Exception("branch product does not exist in the system \n");
        }
        for(String id : _insatnceIds.split(" ")){
            int instanceId = checkIntegrityOfInt(id);
            if(branch.getInstance(instanceId) != null)
                throw new IllegalArgumentException("This instance id already exists.");
            BranchInstance instance = new BranchInstance(instanceId, productId, boughtPrice, expireDate, PlaceOfProduct.InStock, LocalDate.now());
            branch.insertInstance(instance);
        }
    }

    public void submitAmount(int branchId, String _amount, double boughtPrice, String _productId, String _arriveDate) throws Exception {
        int amount = checkIntegrityOfInt(_amount);
        Branch branch = bDAO.getBranch(branchId);
        int productId = checkIntegrityOfInt(_productId);
        LocalDate arriveDate = checkIntegrityOfDate(_arriveDate);

        branch.InsertAmountInstances(amount, boughtPrice, productId, arriveDate);
    }

    public void changeLocationOfInstance(int branchId, String _instanceId, String place) throws Exception{
        int instanceId = checkIntegrityOfInt(_instanceId);
        Branch branch = bDAO.getBranch(branchId);
        if(PlaceOfProduct.fromString(place) == null){
            throw new Exception("place is incorrect");
        }
        branch.changeLocationOfInstance(instanceId, place);
    }

    /*
    This piece of code was taken from the previous Class WorkerService
    */
    public void submitMissingInstance(int branchId, String _insatnceId) throws Exception{
        Branch branch = bDAO.getBranch(branchId);
        int insatnceId = checkIntegrityOfInt(_insatnceId);
        BranchInstance instance = branch.getInstance(insatnceId);
        if(instance == null){
            throw new Exception("insatnce id not exist in the system \n");
        }
        branch.removeInstance(insatnceId);

    }
//finished
    public void reportDefective(int branchId, String _defetiveId, String _description) throws Exception{
        Branch branch = bDAO.getBranch(branchId);
        int defetiveId = checkIntegrityOfInt(_defetiveId);
        BranchInstance instance = branch.getInstance(defetiveId);
        if(instance == null){
            throw new Exception("this instance id dosen't exist in the system");
        }
        instance.setDefected(_description);
        branch.updateInstance(instance);
    }

    public void submitBranchProduct(int branchId, String _productId, String _sellingPrice, String path, String _demand, String placeInStore, String _minimum) throws Exception {
        Branch branch = bDAO.getBranch(branchId);
        int productId = checkIntegrityOfInt(_productId);
        Double sellingPrice = checkIntegrityOfDouble(_sellingPrice);
        int demand = checkIntegrityOfInt(_demand);
        int minimum = checkIntegrityOfInt(_minimum);
        if(sellingPrice < 0){
            throw new Exception("Price must be positive");
        }
        if(demand < 0){
            throw new Exception("Demand must be positive");
        }
        if(minimum < 0){
            throw new Exception("Minimum must be positive");
        }
        if(pDao.getProduct(productId) == null){
            throw new Exception("this product does not exist in the world");
        }
        if(branch.getProduct(productId) != null){
            throw new Exception("a product with this id already exist");
        }
        BranchProduct product = new BranchProduct(productId, sellingPrice, demand, placeInStore, minimum, path);
        branch.insertBranchProduct(product);
    }


    public void updateProductInfo(int branchId, String _productId, String _sellingPrice, String path, String _demand, String placeInStore, String _minimum) throws Exception{
        Branch branch = bDAO.getBranch(branchId);
        int productId = checkIntegrityOfInt(_productId);
        Double sellingPrice = checkIntegrityOfDouble(_sellingPrice);
        int demand = checkIntegrityOfInt(_demand);
        int minimum = checkIntegrityOfInt(_minimum);

        BranchProduct branchProduct = branch.getProduct(productId);
        branchProduct.setInfo(sellingPrice, path, demand, placeInStore, minimum);
        branch.updateProduct(branchProduct);
    }

    /*
    This piece of code was taken from the previous Class UserService
     */
    public boolean registration(String username, String password, String _role, int manually) throws Exception{
        if(username.isBlank()){throw new Exception("username is blank, try again");}
        if(password.isBlank()){throw new Exception("password is blank, try again");}

        UserRole role = UserRole.fromString(_role);
        if(role == null){
            throw new Exception("illegal Role");
        }
        User user = new User(username, password, role);
        if(uDAO.getUser(username) != null){
            throw new Exception("This userName already exist. Please pick another username.\n");
        }
        uDAO.insertNewUser(user);
        return true;
    }


    public boolean login(String username, String password) throws Exception{
        User user = uDAO.getUser(username);
        if(user == null){
            throw new Exception("user not exist\n");
        }
        if(user.isConnected()){
            throw new Exception("user already logged in\n");
        }
        if(!user.getPassword().equals(password)){
            throw new Exception("incorrect password\n");
        }
        user.setConnected(true);
        return true;
    }


    public void logout(String username) throws Exception{

        User user = uDAO.getUser(username);
        if(user == null){
            throw new Exception("user not exist");
        }
        user.setConnected(false);
    }

    public UserRole getRole(String username) throws Exception {
        User user = uDAO.getUser(username);
        if(user == null){
            throw new Exception("User doesn't exist");
        }
        return user.getRole();
    }

    public Branch getBranch(String branchId) throws Exception {
        Integer bracnhId = checkIntegrityOfInt(branchId);
        try {
            return bDAO.getBranch(bracnhId);
        } catch (Exception e) {
            throw new Exception("this branch dosent exist");
        }
    }

    public String getBranchGson(String branchId) throws Exception {
        Integer bracnhId = checkIntegrityOfInt(branchId);
        try {
            return gson.toJson(bDAO.getBranch(bracnhId));
        } catch (Exception e) {
            throw new Exception("this branch dosen't exist");
        }
    }


    public int checkIntegrityOfInt(String _val) throws Exception {
        int val;
        try{
            val = Integer.parseInt(_val);
        }
        catch (Exception e) {
            throw new Exception("nyea that is not an integer\n");
        }
        return val;
    }

    public Double checkIntegrityOfDouble(String _val) throws Exception {
        double val;
        try{
            val = Double.parseDouble(_val);
            val = (int)(Math.round(val*1000))/1000.0;
        }
        catch (Exception e) {
            throw new Exception("nyea that is not a double\n");
        }
        return val;
    }

    private LocalDate checkIntegrityOfDate(String expiredDate) throws Exception {
        LocalDate val;
        try{
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(expiredDate);
            val = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        catch (Exception e) {
            throw new Exception("not in the foramt\n");
        }
        return val;
    }


    public String getProductOnlyIn(int branchId, String path) throws SQLException{
        return gson.toJson(bDAO.getBranch(branchId).getProductOnlyIn(path));
    }

    public String getImmidiateSubCategories(int branchId, String path) throws SQLException{
        return gson.toJson(bDAO.getBranch(branchId).getImmidiateSubCategories(path));
    }

    public void addProduct(String _productId, String name, String manufacturer) throws Exception {
        int productId = checkIntegrityOfInt(_productId);
        if(pDao.getProduct(productId) != null)
            throw new IllegalArgumentException("A product with ID " + productId + " already exists.\n");
        pDao.insertProduct(new Product(productId, name, manufacturer));
    }

    public void AddBranch(int id) throws SQLException {
        bDAO.insertBranch(new Branch(id, LocalDate.now(), 2));
    }


    public String getShortageProductsNames(Map<Integer, Integer> mapShortage) throws SQLException {
        ArrayList<String> returnNames  = new ArrayList<>();
        for(int productId : mapShortage.keySet()){
            Product product = pDao.getProduct(productId);
            returnNames.add(product.getName());
        }
    return gson.toJson(returnNames);
    }

    public String getProduct(int productId) throws SQLException{
        return gson.toJson(pDao.getProduct(productId));
    }

    public void removeProduct(String _productId) throws Exception {
        int productId = checkIntegrityOfInt(_productId);
        if(getProduct(productId) != null){
            pDao.removeProduct(productId);
            for (Branch branch : bDAO.getAll()) {
                branch.removeProduct(productId);
            }
            suppliersController.removeDealProducts(productId);
        }
        else{
            throw new Exception("You have tried to remove a Product that doesn't exist");
        }
    }

    public BranchProduct getBranchProduct(int branchId, int productId) throws SQLException{
        return bDAO.getBranch(branchId).getProduct(productId);
    }

    public String getBranchProductDiscount(int branchId, int productId) throws SQLException{
        return gson.toJson(bDAO.getBranch(branchId).getProduct(productId).getDiscount());
    }

    public void removeBranchProduct(int branchId, int productId) throws SQLException{
        if (getBranchProduct(branchId, productId) != null)
            bDAO.getBranch(branchId).removeProduct(productId);
        else
            System.out.println("You have tried to remove a BranchProduct that doesn't exist");
    }

    public String getProductName(int productId) throws SQLException {
        return pDao.getProduct(productId).getName();
    }
    public BranchInstance getBranchInstance(int branchId, int instanceId) throws SQLException{
        return bDAO.getBranch(branchId).getInstance(instanceId);
    }

    public BranchInstance getBranchInstanceString(int branchId, String instanceId) throws Exception{
        return bDAO.getBranch(branchId).getInstance(checkIntegrityOfInt(instanceId));
    }
    public void removeBranchInstance(int branchId, int instanceId) throws SQLException{
        if (getBranchInstance(branchId, instanceId) != null)
            bDAO.getBranch(branchId).removeInstance(instanceId);
        else
            System.out.println("You have tried to remove a BranchProduct that doesn't exist");
    }


    public void UpdateProduct(String _productid, String name, String manufacturer) throws Exception{
        int productid = checkIntegrityOfInt(_productid);
        Product p = new Product(productid, name, manufacturer);
        pDao.updateProduct(p);
    }

    public void updateInstanceExpiredDate(int branchId, String _instanceId, String expiredDate) throws Exception {
        Branch branch = bDAO.getBranch(branchId);
        int instanceId = checkIntegrityOfInt(_instanceId);
        BranchInstance instance = branch.getInstance(instanceId);
        LocalDate expireDate;
        if(expiredDate.equals("-")){
            expireDate = checkIntegrityOfDate("9999-01-01");
        }
        else{
            expireDate = checkIntegrityOfDate(expiredDate);
        }
        instance.setExpireDate(expireDate);
        bDAO.getBranch(branchId).updateInstance(instance);
    }
    
}
