package stock.presentation;

import java.util.Scanner;

public class Reader {
    static Scanner reader = new Scanner(System.in);

    public static String read(String msg){
        System.out.print(msg);
        return reader.nextLine();
    }
}
