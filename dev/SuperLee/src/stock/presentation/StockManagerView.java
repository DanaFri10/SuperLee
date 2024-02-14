package stock.presentation;


import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.LocalDateAdapter;
import stock.business.StockController;
import suppliers.business.Contact;
import suppliers.business.Order;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;
import suppliers.presentation.OrderHistoryView;

public class StockManagerView {
    public MainView mainView;
    private int currentBranchId;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();



    public StockManagerView(int _currentBranchId, MainView _mainView) {
        mainView = _mainView;
        currentBranchId = _currentBranchId;
        
    }

    public void start(StockController sc, SuppliersController spc) throws Exception {
        try {
            Writer.print(sc.createDefectivesReport(currentBranchId));
        } catch (Exception e) {
            Writer.print(e.getMessage());
        }


        outer : while(true){
            if(mainView.end){
                break outer;
            }
            Writer.print("\n");
            Writer.print("plz select your wanted action\n");
            Writer.print("write (1) for creating a selling discount\n");
            Writer.print("write (2) to see data on category\n");
            Writer.print("write (3) to set the time period between receiving an update on defectives\n");
            Writer.print("write (4) to see data on inventory\n");
            Writer.print("write (5) to see products which are about to run out\n");
            Writer.print("write (6) to update info on branch Product\n");
            Writer.print("write (7) to delete Product (in world)\n");
            Writer.print("write (8) to update Product (in world)\n");
            Writer.print("write (9) to explore Branch Products\n");
            Writer.print("write (10) to access Suppliers Module\n");
            Writer.print("write (11) to access wareHouseView\n");
            Writer.print("write (12) to see order history\n");


            Writer.print("write (back) to return to the last menu\n");
            Writer.print("write (end) to quit\n");
            String answer = Reader.read("your answer: ");
            switch(answer){
                case("1"):
                    startSellingDiscountview(sc);
                    break ;
                case("2"):
                    startCategoryview(sc);
                    break ; 
                case("3"):
                    startSetTimePeriod(sc);
                    break ;
                case("4"):
                    startInventoryview(sc);
                    break ;
                case("5"):
                    shortageReport(sc, spc);
                    break ;
                case("6"):
                    updateBranchProductInfo(sc);
                    break ;
                case("7"):
                    DeleteProduct(sc);
                    break ;
                case("8"):
                    UpdateProduct(sc);
                    break ;
                case("9"):
                    ProductExplorer pe = new ProductExplorer(sc,currentBranchId);
                    pe.start();
                    break ;
                case("10"):
                    suppliers.presentation.MainView mv = new suppliers.presentation.MainView(spc);
                    mv.start();
                    break ;
                case("11"):
                    WarehouseWorkerView wwv = new WarehouseWorkerView(currentBranchId, mainView);
                    wwv.start(sc,spc);
                    break ;
                case("12"):
                    OrderHistoryView ohw = new OrderHistoryView(currentBranchId,spc);
                    ohw.start();
                    break;
                case("back"):
                    break outer; 
                case("end"):
                    end(); 
                    break outer; 
                default:
                    Writer.print("nyea that is not an option");        
            }
        }
    }

    private void UpdateProduct(StockController sc) {

        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("Then Enter the Product id, name, manufacturer\n");
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
        outer : while(true){
            String answer = Reader.read("");
            switch(answer){
                case("back"):
                    break outer;
                case("end"):
                    end();
                    break outer;
                default:
                    String productid = Reader.read("product id: ");
                    String name = Reader.read("name: ");
                    String manufacturer = Reader.read("manufacturer: ");

                    try {
                        sc.UpdateProduct(productid, name, manufacturer);
                        Writer.print("\nsuccess\n");
                    } catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }
    }

    private void DeleteProduct(StockController sc) {
        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("Then Enter the Product id\n");
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
        outer : while(true){
            String answer = Reader.read("");
            switch(answer){
                case("back"):
                    break outer;
                case("end"):
                    end();
                    break outer;
                default:
                    String productid = Reader.read("product id: ");

                    try {
                        sc.removeProduct(productid);
                        Writer.print("\nsuccess\n");
                    } catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }
    }

    private void updateBranchProductInfo(StockController sc){
        Writer.print("please insert the following information to the system \n");

        Writer.print("press enter to insert data in each iteration \n");
        Writer.print("press back to end the function \n");
        Writer.print("press end to quit the StockManagerView \n");
        outer : while(true){
            String answer = Reader.read("");
            switch(answer){
                case("back"):
                    break outer;
                case("end"):
                    end();
                    break outer;
                default:
                    String productid = Reader.read("product id: ");
                    String sellingPrice = Reader.read("sellingPrice: ");
                    String path = Reader.read("path: ");
                    String demand = Reader.read("demand: ");
                    String placeInStore = Reader.read("placeInStore: ");
                    String minimum = Reader.read("minimum: ");
                    try {
                        sc.updateProductInfo(currentBranchId, productid, sellingPrice, path, demand, placeInStore, minimum);
                        Writer.print("\nsuccess\n");
                    } catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }
    }

    private void shortageReport(StockController sc, SuppliersController spc) {
        try {
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            Map<Integer, Integer> mapShortage = gson.fromJson(sc.getShortage(currentBranchId), intToint);
            Writer.print(sc.createShortageReport(currentBranchId, mapShortage));
            if(!mapShortage.isEmpty()){
                String deployOrder = Reader.read("want to generate order? (y/n)");
                if(deployOrder.equals("y")){
                    Writer.print("plz enter the extra amount for each product you want to order\n");
                    Writer.print("press enter to start\n");
                    Reader.read("");
                    Map<Integer, Integer> extras = new HashMap<>();
                    for(int productId : mapShortage.keySet()){
                        extras.put(productId,Integer.parseInt(Reader.read(sc.getProductName(productId) + ": ")));
                    }
                    String orderString = spc.makeShortageOrder(mapShortage, extras,currentBranchId);
                    Order order = gson.fromJson(orderString, Order.class);
                    System.out.println("The system has calculated the following order to be the fastest:");
                    System.out.println(spc.describeOrder(order.getOrderId()));
                    addContacts(order.getOrderId(), spc);
                }
            }
        } catch (Exception e) {
            Writer.print(e.getMessage());
        }
    }

    private void startInventoryview(StockController sc) {
        Writer.print("write (see) to see the inventory report\n");
        Writer.print("Remainder.\n");  
        Writer.print("To go back to the last menu write back\n");  
        Writer.print("To end interaction write end\n"); 
        outer : while(true){
            String answer = Reader.read("");
            switch(answer){              
                case("back"):
                    break outer; 
                case("end"):
                    end(); 
                    break outer;
                case("see"):
                    try {
                        Writer.print(sc.createInventoryReport(currentBranchId));
                    } catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            } 
        }   
    }

    private void startSellingDiscountview(StockController sc){
        Writer.print("press enter to start and press enter between discounts creations\n");
        Writer.print("Then Enter the percentage of the discount \n");
        Writer.print("press 1 for discount on a Category or 2 for a Product \n");
        Writer.print("also enter the starting date and the ending date of the product \n");
        Writer.print("at last if its a discount on product enter the product id. \n");
        Writer.print("if its a category enter the hierarchy of the category you want to discount for example:\n");
        Writer.print("'Dairy/Milk' will give discount to all Milk products\n");
        Writer.print("Remainder.\n");  
        Writer.print("To go back to the last menu write back\n");  
        Writer.print("To end interaction write end\n");  
        outer : while(true){
            String answer = Reader.read("");
            switch(answer){              
                case("back"):
                    break outer; 
                case("end"):
                    end(); 
                    break outer; 
                default:
                    String precentage = Reader.read("precentage: ");
                    String categoryOrProduct = Reader.read("1 for Category , 2 for Product: ");
                    String startingDate = Reader.read("starting date - enter in the format of YYYY-MM-dd: ");
                    String endingDate = Reader.read("ending date - enter in the format of YYYY-MM-dd: ");
                    String nameOrId = Reader.read("enter product id or categories name: ");
                    try{
                        sc.createSellingDiscount(currentBranchId, precentage, categoryOrProduct, startingDate, endingDate, nameOrId);
                        Writer.print("new Discount has entered successfully !\n");
                    }
                    catch (Exception e) {
                        Writer.print(e.getMessage());
                    }    
            } 
        }
    }

    private void startCategoryview(StockController sc) {
        Writer.print("press enter to start and press enter between category viewing\n");
        Writer.print("Then Enter the category you want to view (enter by the category you want to see by inserting them\n");
        Writer.print("like this: if you wanna check 350ml of milk of dairy enter: Dairy/Milk/350ml\n");
        Writer.print("if you want to see multiple categories press enter to submit a new one, to stop type (stop)\n");
        Writer.print("Remainder.\n");  
        Writer.print("To go back to the last menu write back\n");  
        Writer.print("To end interaction write end\n");  
        outer : while(true){
            String answer = Reader.read("");
            ArrayList<String> categories = new ArrayList<>();
            switch(answer){              
                case("back"):
                    break outer; 
                case("end"):
                    end(); 
                    break outer; 
                default:
                    String category = Reader.read("categories name: ");
                    while(!category.equals("stop")){
                        categories.add(category);
                        category = Reader.read("categories name: ");
                    }

                    try{
                        Writer.print(sc.createCategoryReport(currentBranchId, categories));
                    }
                    catch (Exception e) {
                        Writer.print(e.getMessage());
                    }    
            } 
        }
    }


    private void startSetTimePeriod(StockController sc){
        Writer.print("press enter to start");
        Writer.print("plz enter the time step in which you want to receive updates on defects");  
        Writer.print("Remainder.\n");  
        Writer.print("To go back to the last menu write back\n");  
        Writer.print("To end interaction write end\n");  
        Writer.print("not after entering the timestep you will be returend to the last menu\n");  
        Writer.print("\n");
        outer : while(true){
            String answer = Reader.read("");
            switch(answer){              
                case("back"):
                    break outer; 
                case("end"):
                    end(); 
                    break outer; 
                default:
                    String timeStep = Reader.read("time step: ");
                    try{
                        sc.updateTimeStep(currentBranchId, timeStep);
                        break outer;
                    }
                    catch (Exception e) {
                        Writer.print(e.getMessage());
                    }    
            } 
        }
    }

    private void addContacts(int orderId, SuppliersController sc) throws SQLException {
        String json = sc.getSupplierIdsInOrder(orderId);
        Type listType = new TypeToken<LinkedList<String>>(){}.getType();
        List<String> supplierIds = gson.fromJson(json, listType);
        for(String supplierId : supplierIds){
            Supplier supplier = gson.fromJson(sc.getSupplier(supplierId), Supplier.class);
            if(sc.hasContacts(supplierId)){
                System.out.println("Please choose a contact of " + supplier.getCompanyName() + " to add to this order:");
                int index = 1;
                String contactsJson = sc.getSupplierContacts(supplierId);
                Type contactsListType = new TypeToken<LinkedList<Contact>>(){}.getType();
                List<Contact> contacts = gson.fromJson(contactsJson, contactsListType);
                for (Contact cm : contacts) {
                    System.out.println(index + ". " + cm.getName() + ", " + cm.getPhoneNumber() + " via " + cm.getEmail());
                    index++;
                }
                boolean accepted = false;
                int chosenIndex = 0;
                while (!accepted){
                    try {
                        chosenIndex = Integer.parseInt(Reader.read("")) - 1;
                        if(chosenIndex < 0 || chosenIndex >= contacts.size()){
                            System.out.println("Please choose a contact from the list");
                        }else{
                            accepted = true;
                        }
                    }catch (Exception e){
                        System.out.println("Not a number");
                    }
                }
                Contact chosen = contacts.get(chosenIndex);
                try {
                    sc.addContactToOrder(orderId,supplierId,chosen.getContactId());
                    if(Reader.read("Would you like to update this contact about the order via Email? Y/N").charAt(0) == 'y'){
                        sc.updateContactOrderMade(supplierId,chosen.getContactId(),orderId);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public void end(){
        mainView.end();
    }
}
