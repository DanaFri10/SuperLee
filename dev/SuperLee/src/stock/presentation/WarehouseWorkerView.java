package stock.presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.LocalDateAdapter;
import stock.business.StockController;
import suppliers.business.Contact;
import suppliers.business.Order;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;
import suppliers.presentation.CreateOrderView;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WarehouseWorkerView {
    public MainView mainView;
    private int currentBranchId;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();

    public WarehouseWorkerView(int branchId, MainView mainView2) {
        currentBranchId = branchId;
        mainView = mainView2;

    }


    public void start(StockController sc, SuppliersController spc){
        outer : while(true){
            if(mainView.end){
                break outer;
            }
            Writer.print("plz select your wanted action\n");
            Writer.print("write (1) for submitting missing Instances\n");
            Writer.print("write (2) for reporting defectives products\n");
            Writer.print("write (3) to add a new instance of a product to the system\n");
            Writer.print("write (4) to change the location of an instance of product (stock/store)\n");
            Writer.print("write (5) change expire date of a product\n");
            Writer.print("write (6) to see products which are about to run out\n");
            Writer.print("write (7) to make an order\n");

            Writer.print("write (back) to return to the last menu\n");
            Writer.print("write (end) to quit\n");
            Writer.print("\n");
            String answer = Reader.read("your answer: ");
            Writer.print("\n");
            switch(answer){
                case("1"):
                    startSubmitMissingInstanceView(sc);
                    break;
                case("2"):
                    startReportDefectiveView(sc);
                    break;
                case("3"):
                    startAddingInstance(sc);
                    break;
                case("4"):
                    changeLocationOfInstance(sc);
                    break;
                case("5"):
                    giveInstanceExpiredDate(sc);
                    break;
                case("6"):
                    shortageReport(sc,spc);
                    break;
                case("7"):
                    CreateOrderView cov = new CreateOrderView(spc,currentBranchId);
                    cov.start();
                    break;
                case("back"):
                    break outer;
                case("end"):
                    end();
                    break outer;
                default:
                    Writer.print("Nyea, that is not an option");
            }
        }
    }

    private void giveInstanceExpiredDate(StockController sc) {
        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("Then Enter the instance id, and expire date\n");
        Writer.print("note. date should be in the format YYYY-MM-dd\n" );
        Writer.print("note. if there is no date write (-)\n" );
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
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
                    String instanceId = Reader.read("instance id: ");
                    String expiredDate = Reader.read("expire date: ");
                    try{
                        sc.updateInstanceExpiredDate(currentBranchId, instanceId , expiredDate);
                        Writer.print("item was changed successfully\n");
                    }
                    catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }
    }


    private void startAddingInstance(StockController sc){
        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("Then Enter the insatnce id, bought price, product id, and expire date\n");
        Writer.print("note. date should be in the format YYYY-MM-dd\n" );
        Writer.print("note. if there is no date write (-)\n" );
        Writer.print("note. you can enter multiple instances with same date and place\n by inserting in the instance id multiple id's with space between each one\n" );
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
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
                    String insatnceId = Reader.read("insatnce id: ");
                    String boughtPrice = Reader.read("boughtPrice: ");
                    String productId = Reader.read("product Id: ");
                    String expiredDate = Reader.read("expire date: ");
                    try{
                        sc.submitInstances(currentBranchId, insatnceId , boughtPrice, productId, expiredDate);
                        Writer.print("items were added successfully\n");
                    }
                    catch (Exception e) {
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

    public void startSubmitMissingInstanceView(StockController sc){
        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("Then Enter the instance id\n");
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
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
                    String instanceId = Reader.read("instance id: ");
                    try{
                        sc.submitMissingInstance(currentBranchId, instanceId);
                        Writer.print("item removed successfully\n");
                    }
                    catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }
    }

    public void startReportDefectiveView(StockController sc){
        Writer.print("press enter to start and press enter between submits\n");
        Writer.print("Then Enter the defetive id and a description for the defectivity\n");
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
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
                    String defetiveId = Reader.read("defetive id: ");
                    String description = Reader.read("description: ");
                    try{
                        sc.reportDefective(currentBranchId, defetiveId, description);
                        Writer.print("item was changed successfully\n");
                    }
                    catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }
    }

    public void changeLocationOfInstance(StockController sc){
        Writer.print("Remainder.\n");
        Writer.print("To go back to the last menu write back\n");
        Writer.print("To end interaction write end\n");
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
                    String instanceId = Reader.read("enter instanceId: ");
                    String place = Reader.read("enter place (stock/store): ");
                    try {
                        sc.changeLocationOfInstance(currentBranchId, instanceId, place);
                        Writer.print("item was changed successfully");
                    } catch (Exception e) {
                        Writer.print(e.getMessage());
                    }
            }
        }


    }

    public void end(){
        mainView.end();
    }

}
