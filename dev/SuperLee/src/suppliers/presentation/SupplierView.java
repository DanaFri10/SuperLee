package suppliers.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import suppliers.business.Contact;
import suppliers.business.PaymentMethod;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SupplierView implements Menu{
    private String supplierId;
    private boolean exit;
    private Scanner scanner;
    private SuppliersController sc;
    private Supplier model;
    private Gson gson;
    public SupplierView(String supplierId,SuppliersController sc){
        this.supplierId = supplierId;
        this.sc = sc;
        this.exit = false;
        this.scanner = new Scanner(System.in);
        gson = new Gson();
    }

    @Override
    public void start() {
        try {
            String supplierJson = sc.getSupplier(supplierId);
            this.model = gson.fromJson(supplierJson, Supplier.class);
            while(!exit){
                System.out.println(MainView.BAR);
                showSupplierInfo();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showSupplierInfo() throws SQLException {
        System.out.println("Showing information about " + model.getCompanyId() + " " + model.getCompanyName());
        System.out.println("Company ID: " + model.getCompanyId());
        System.out.println("Company Name: " + model.getCompanyName());
        System.out.println("Location: " + model.getLocation());
        System.out.println("Bank Account: " + model.getBankAccount());
        System.out.print("Specializes in: ");
        System.out.println(model.getSupplyAreas().stream().collect(Collectors.joining(",")));
        System.out.println();
        System.out.println("Contacts:");
        int contactIndex = 1;
        String contactsJson = sc.getSupplierContacts(supplierId);
        Type listType = new TypeToken<LinkedList<Contact>>(){}.getType();
        List<Contact> supplierContacts = gson.fromJson(contactsJson, listType);
        for(Contact contactData : supplierContacts){
            System.out.println(contactIndex + ".");
            System.out.println("Name: " + contactData.getName());
            System.out.println("ID: " + contactData.getContactId());
            System.out.println("Address: " + contactData.getAddress());
            System.out.println("Phone Number: " + contactData.getPhoneNumber());
            System.out.println("Email: " + contactData.getEmail());
            contactIndex++;
        }
        System.out.println("\nOptions:");
        System.out.println("1. Return to list");
        System.out.println("2. Show deal with " + model.getCompanyId() + " " + model.getCompanyName());
        System.out.println("3. Remove " + model.getCompanyId() + " " + model.getCompanyName());
        System.out.println("4. Add contact");
        System.out.println("5. Remove contact");
        System.out.println("6. Update contact details");
        System.out.println("7. Alter Deal with " + model.getCompanyId() + " " + model.getCompanyName());
        System.out.println("8. Update supplier details");
        System.out.println("9. Show order history from this supplier");
        boolean accepted = false;
        try {
            while (!accepted) {
                String command = scanner.nextLine();
                switch (command) {
                    case "1":
                        exit = true;
                        accepted = true;
                        break;
                    case "2":
                        accepted = true;
                        showDeal();
                        break;
                    case "3":
                        accepted = true;
                        sc.removeSupplier(supplierId);
                        exit = true;
                        break;
                    case "4":
                        accepted = true;
                        addContact();
                        break;
                    case "5":
                        accepted = true;
                        removeContact();
                        break;
                    case "6":
                        accepted = true;
                        updateContact();
                        break;
                    case "7":
                        accepted = true;
                        newDeal();
                        break;
                    case "8":
                        accepted = true;
                        updateSupplierInfo();
                        break;
                    case "9":
                        accepted = true;
                        showOrderHistory();
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void showDeal() throws Exception {
        DealView dw = new DealView(supplierId,sc);
        dw.start();
    }

    private void addContact(){
        System.out.println("Create new contact or choose from list?");
        System.out.println("1. Create a new contact");
        System.out.println("2. Choose from list");
        boolean accepted = false;
        while(!accepted) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    accepted = true;
                    System.out.println("ID?");
                    String id = scanner.nextLine();
                    System.out.println("Name?");
                    String name = scanner.nextLine();
                    System.out.println("Phone Number? (0AA-DDDDDDD)");
                    String phoneNumber = scanner.nextLine();
                    System.out.println("Email?");
                    String email = scanner.nextLine();
                    System.out.println("Address?");
                    String address = scanner.nextLine();sc.addNewSupplierContact(supplierId,id, name, phoneNumber, email, address);
                } else if (choice == 2) {
                    accepted = true;
                    String contactsJson = sc.getAllContacts();
                    Type listType = new TypeToken<LinkedList<Contact>>(){}.getType();
                    List<Contact> contacts = gson.fromJson(contactsJson, listType);
                    int index = 1;
                    for (Contact cm : contacts) {
                        System.out.println(index + ".");
                        System.out.println("Name: " + cm.getName());
                        System.out.println("ID: " + cm.getContactId());
                        System.out.println("Address: " + cm.getAddress());
                        System.out.println("Phone Number: " + cm.getPhoneNumber());
                        System.out.println("Email: " + cm.getEmail());
                        index++;
                    }
                    Contact cm = contacts.get(Integer.parseInt(scanner.nextLine()) - 1);
                    sc.addExistingSupplierContact(supplierId,cm.getContactId());
                }else{
                    System.out.println("Unknown command");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void makePeriodicOrder() throws SQLException {
        Map<Integer, Integer> productsMap = new HashMap<>();
        boolean done = false;
        System.out.println("What branch is the periodic order to?");
        int branchId = Integer.parseInt(scanner.nextLine());
        System.out.println("Please specify the product ID, amount and suppliers of the products you want to order. Type 'Done' when you're done.");
        while(!done){
            System.out.println("Product ID?");
            String product = scanner.nextLine();
            if(product.toLowerCase().equals("done")){
                done = true;
                break;
            }
            try {
                int productInt = Integer.parseInt(product);
                if(!sc.productExists(productInt)){
                    System.out.println("Product with ID " + productInt + " does not exist");
                    break;
                }
                System.out.println("Amount?");
                String amount = scanner.nextLine();
                if(amount.toLowerCase().equals("done")){
                    done = true;
                    break;
                }
                try {
                    int amountInt = Integer.parseInt(amount);
                    if(productsMap.containsKey(productInt)){
                        productsMap.put(productInt, productsMap.get(productInt)+amountInt);
                    }else{
                        productsMap.put(productInt, amountInt);
                    }
                }
                catch (Exception e) {
                    System.out.println("Not a number.");
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        sc.setPeriodicOrderProducts(supplierId,branchId,productsMap, true);
    }

    private void newDeal() throws Exception {
        System.out.println("WARNING: This will override any existing deal. Do you wish to continue? Y/N");
        String response = scanner.nextLine();
        if(response.toLowerCase().charAt(0) != 'y'){
            return;
        }
        System.out.println("Will the supplier deliver the orders on its own? Y/N");
        response = scanner.nextLine();
        boolean delivers = response.toLowerCase().charAt(0) == 'y';
        System.out.println("How many days will the supplier take to get an order ready?");
        int daysToDeliver = Integer.parseInt(scanner.nextLine());
        boolean[] deliveryDays = new boolean[]{false, false, false, false, false, false, false};
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
                for (String number : numbers) {
                    deliveryDays[Integer.parseInt(number) - 1] = true;
                }
            }
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
        sc.updateDeal(supplierId,delivers,daysToDeliver,PaymentMethod.values()[number-1],deliveryDays);
        System.out.println("Basic deal information has been added, you can now add products and discounts in the \"Show Deal\" menu");
        if(sc.hasContacts(supplierId)) {
            System.out.println("Would you like to update a contact about the new deal via email? Y/N");
            response = scanner.nextLine();
            if (response.toLowerCase().charAt(0) == 'y') {
                updateContactDealAltered();
            }
        }
    }

    private void updateSupplierInfo() throws Exception {
        System.out.println("New Name? (press enter to keep the same)");
        String name = scanner.nextLine();
        System.out.println("New Location? (press enter to keep the same)");
        String location = scanner.nextLine();
        System.out.println("New Bank Account? (BBB-CCCCC/DD) (press enter to keep the same)");
        String bank = scanner.nextLine();
        System.out.println("Choose New Payment Details");
        System.out.println("In what areas does the supplier specialize?");
        System.out.println("Write the areas separated by commas or press enter to keep unchanged");
        String supplyAreasString = scanner.nextLine().replaceAll(", ", ",");
        sc.updateSupplierInfo(supplierId,
                name.isEmpty() ? model.getCompanyName() : name,
                location.isEmpty() ? model.getLocation() : location,
                bank.isEmpty() ? model.getBankAccount() : bank,
                supplyAreasString.isEmpty() ? model.getSupplyAreas() : Arrays.asList(supplyAreasString.split(","))
        );
    }

    private void updateContactDealAltered() throws SQLException {
        System.out.println("Please choose a contact to update:");
        int index = 1;
        String contactsJson = sc.getSupplierContacts(supplierId);
        Type listType = new TypeToken<LinkedList<Contact>>(){}.getType();
        List<Contact> supplierContacts = gson.fromJson(contactsJson, listType);
        for(Contact contactData : supplierContacts){
            System.out.println(index + ". " + contactData.getName() + " via " + contactData.getEmail());
            index++;
        }
        int chosenIndex = Integer.parseInt(scanner.nextLine()) - 1;
        String chosenId = supplierContacts.get(chosenIndex).getContactId();
        try {
            sc.updateContactDealUpdated(supplierId,chosenId);
            System.out.println("Email has been sent!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeContact() throws Exception {
        System.out.println("Please choose a contact to remove:");
        int index = 1;
        String contactsJson = sc.getSupplierContacts(supplierId);
        Type listType = new TypeToken<LinkedList<Contact>>(){}.getType();
        List<Contact> supplierContacts = gson.fromJson(contactsJson, listType);
        for(Contact contactData : supplierContacts){
            System.out.println(index + ". " + contactData.getName() + " via " + contactData.getEmail());
            index++;
        }
        int chosenIndex = Integer.parseInt(scanner.nextLine()) - 1;
        String chosenId = supplierContacts.get(chosenIndex).getContactId();
        sc.removeSupplierContact(supplierId,chosenId);
    }

    private void updateContact() throws Exception {
        System.out.println("Please choose a contact to update details of:");
        int index = 1;
        String contactsJson = sc.getSupplierContacts(supplierId);
        Type listType = new TypeToken<LinkedList<Contact>>(){}.getType();
        List<Contact> supplierContacts = gson.fromJson(contactsJson, listType);
        for(Contact contactData : supplierContacts){
            System.out.println(index + ". " + contactData.getName() + " via " + contactData.getEmail());
            index++;
        }
        int chosenIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Contact chosen = supplierContacts.get(chosenIndex);
        System.out.println("New Name? (Press enter to keep the same)");
        String name = scanner.nextLine();
        System.out.println("New Phone Number? (0AA-DDDDDDD) (Press enter to keep the same)");
        String phoneNumber = scanner.nextLine();
        System.out.println("New Email? (Press enter to keep the same)");
        String email = scanner.nextLine();
        System.out.println("New Address? (Press enter to keep the same)");
        String address = scanner.nextLine();
        sc.updateContact(
                chosen.getContactId(),
                name.isEmpty() ? chosen.getName() : name,
                phoneNumber.isEmpty() ? chosen.getPhoneNumber() : phoneNumber,
                email.isEmpty() ? chosen.getEmail() : email,
                address.isEmpty() ? chosen.getAddress() : address
        );
    }

    private void showOrderHistory() throws Exception {
        System.out.println(sc.orderHistoryForSupplier(supplierId));
    }

    /*
    private void addSupplyArea(){
        System.out.println("What supply area would you like to add?");
        String supplyArea = scanner.nextLine();
        try {
            model.addSupplyArea(supplyArea);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void removeSupplyArea(){
        System.out.println("What supply area would you like to remove?");
        String supplyArea = scanner.nextLine();
        try {
            model.removeSupplyArea(supplyArea);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }*/
}
