package suppliers.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import suppliers.business.PaymentMethod;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;

import java.lang.reflect.Type;
import java.util.*;

public class SupplierListView implements Menu{
    private SuppliersController sc;
    private Gson gson;
    private Scanner scanner;
    private boolean exit;

    public SupplierListView(SuppliersController sc){
        this.sc = sc;
        gson = new Gson();
        scanner = new Scanner(System.in);
        exit = false;
    }

    @Override
    public void start() {
        while (!exit){
            System.out.println(MainView.BAR);
            showSuppliers();
        }
    }

    public void addSupplier(){
        System.out.println("ID? (must be 9 numbers long)");
        String id = scanner.nextLine();
        System.out.println("Name?");
        String name = scanner.nextLine();
        System.out.println("Location?");
        String location = scanner.nextLine();
        System.out.println("Bank Account? (BBB-CCCCC/DD)");
        String bank = scanner.nextLine();
        System.out.println("In what areas does the supplier specialize?");
        System.out.println("Write the areas separated by commas");
        String supplyAreasString = scanner.nextLine().replaceAll(", ", ",");
        System.out.println("Please add the required information about the deal with the supplier");
        System.out.println("Will the supplier deliver the orders on its own? Y/N");
        String yNresponse = scanner.nextLine();
        boolean delivers = yNresponse.toLowerCase().charAt(0) == 'y';
        boolean[] deliveryDays = new boolean[7];
        boolean needsDays = true;
        if(delivers) {
            System.out.println("What days of the week will the supplier supply on?");
            System.out.println("Write the numbers of the days, as listed, separated by commas");
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            for (int i = 0; i < 7; i++) {
                System.out.println((i + 1) + ". " + days[i]);
            }

            System.out.println("Example: 1,4 ==> Supplier delivers on sundays and wednesdays");
            System.out.println("Simply press enter if the supplier does not deliver on fixed days");
            String daysResponse = scanner.nextLine();
            if (!daysResponse.isEmpty()) {
                String[] numbers = daysResponse.split(",");
                for (String dayNumber : numbers) {
                    deliveryDays[Integer.parseInt(dayNumber) - 1] = true;
                }
                needsDays = false;
            }
        }
        int daysToDeliver = -1;
        if(needsDays) {
            System.out.println("How many days will the supplier take to get an order ready?");
            daysToDeliver = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Choose Payment Details");
        int index = 1;
        for(PaymentMethod method : PaymentMethod.values()){
            System.out.print(index + ". ");
            System.out.println(method.name());
            index++;
        }
        boolean accepted = false;
        int number = 0;
        while(!accepted){
            String numberString = scanner.nextLine();
            try{
                number = Integer.parseInt(numberString);
                if(number < 1 || number > PaymentMethod.values().length){
                    System.out.println("Please choose a payment method from the list");
                }else{
                    accepted = true;
                }
            }catch (Exception e){
                System.out.println("Not a number");
            }
        }
        try {
            sc.addSupplier(id,name,location,bank,PaymentMethod.values()[number-1], Arrays.asList(supplyAreasString.split(",")),delivers,daysToDeliver,deliveryDays);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void filterSuppliers()
    {
        System.out.println("What supply area would you like to filter suppliers by?");
        String supplyArea = scanner.nextLine();
        try{
            String json = sc.filterBySupplyArea(supplyArea);
            Type listType = new TypeToken<LinkedList<String>>(){}.getType();
            List<String> supIds = gson.fromJson(json, listType);
            for(String supplierJson : supIds){
                System.out.println(supplierJson);
            }
            System.out.println();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void showSuppliers(){
        try {
            String json = sc.getAllSuppliers();
            Type listType = new TypeToken<LinkedList<Supplier>>(){}.getType();
            List<Supplier> suppliers = gson.fromJson(json, listType);
            int listIndex = 1;
            for (Supplier supplier : suppliers) {
                System.out.println(listIndex + ". " + supplier.getCompanyName() + " (" + supplier.getCompanyId() + ")");
                listIndex++;
            }
            System.out.println("Enter list number to see info about suppliers, 'add' to add a new supplier, 'filter' to filter suppliers or 'exit' to go back to main menu");
            boolean accepted = false;
            while (!accepted) {
                String command = scanner.nextLine();
                if (command.equals("add")) {
                    accepted = true;
                    addSupplier();
                }
                else if (command.equals("filter")) {
                    accepted = true;
                    filterSuppliers();
                }
                else if (command.equals("exit")) {
                    accepted = true;
                    exit = true;
                } else {
                    try {
                        int index = Integer.parseInt(command);
                        if (index > suppliers.size() || index < 1) {
                            System.out.println("Number not listed, try again");
                        } else {
                            accepted = true;
                            chooseSupplier(suppliers.get(index-1).getCompanyId());
                        }
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Unknown command");
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            exit=true;
        }
    }

    private void chooseSupplier(String supplierId){
        SupplierView sw = new SupplierView(supplierId,sc);
        sw.start();
    }
}
