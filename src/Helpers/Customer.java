package Helpers;


import java.util.Scanner;

public class Customer {
    public static void main(String[] args) {
        System.out.println("Enter your name:");
        try
        {   Scanner in = new Scanner(System.in);
            try (Client client = new Client(in.nextLine()))
            {   System.out.println("Logged in successfully.");
                System.out.println("Please input 'order * tea/teas and * coffee/coffees' to order");
                System.out.println("Or input 'exit' to exit system");
                System.out.println("Or input 'Order status' to check order status");
                //Handle SIGMENT
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        System.exit(0);
                };});
                while (true)
                {client.call_Function(in.nextLine());}
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

}
