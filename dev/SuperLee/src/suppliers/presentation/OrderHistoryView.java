package suppliers.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import suppliers.business.Order;
import suppliers.business.Supplier;
import suppliers.business.SuppliersController;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class OrderHistoryView implements Menu{
    private SuppliersController sc;
    private int branchId;
    private Gson gson;
    private Scanner scanner;
    private boolean exit;

    public OrderHistoryView(int branchId, SuppliersController sc){
        this.sc = sc;
        this.branchId = branchId;
        gson = new Gson();
        scanner = new Scanner(System.in);
        exit = false;
    }

    @Override
    public void start() {
        while (!exit){
            System.out.println(MainView.BAR);
            showHistory();
        }
    }

    private void showHistory(){
        try {
            String history = sc.branchOrderHistory(branchId);
            System.out.println(history);
            System.out.println("Type 'exit' to go back to main menu");
            boolean accepted = false;
            while (!accepted) {
                String command = scanner.nextLine();
                if (command.equals("exit")) {
                    accepted = true;
                    exit = true;
                } else {
                    System.out.println("Unknown command");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            exit = true;
        }
    }

}
