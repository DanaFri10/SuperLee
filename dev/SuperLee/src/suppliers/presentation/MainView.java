package suppliers.presentation;

import shared.ControllerFactory;
import shared.Database;
import suppliers.business.SuppliersController;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Scanner;

public class MainView implements Menu{
    private SuppliersController sc;
    private ControllerFactory cf;
    private Scanner scanner;
    private boolean exit;
    public static String BAR = "===============================";

    public MainView(SuppliersController sc) throws Exception {
        this.sc = sc;
        scanner = new Scanner(System.in);
        exit = false;
    }
    public void start(){
        while(!exit){
            goToNextMenu();
        }
    }

    private void goToNextMenu(){
        Menu nextMenu = null;
        while (nextMenu == null) {
            System.out.println("Welcome to the SuperLee Suppliers Module");
            nextMenu = new SupplierListView(sc);
            exit = true;
        }
        nextMenu.start();
    }

}
