package Helpers;

import Helpers.Barista;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;



public class ClientHandler implements Runnable {
    private final Socket socket;
    private final User user;

    Thread writer_Barista;
    Thread completed_Order;


    public ClientHandler(Socket socket) {

        this.socket = socket;
        Barista.number_Client +=1;
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
            String name_User = scanner.nextLine();
            User user = OrderHandler.checkUser(name_User);
            if (user == null) {
                user = new User(name_User);
                OrderHandler.initialUser(user);
            }
            this.user = user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void run() {
        try (Scanner scanner = new Scanner(socket.getInputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {

            try{
                completed_Order = new Thread(() -> {
                    while (true) {
                        /*System.out.println("-------------------------------");
                        System.out.println(user.coffee_waiting);
                        System.out.println(user.tea_waiting);
                        System.out.println(user.coffee_brewing);
                        System.out.println(user.tea_brewing);
                        System.out.println(user.tea_tray);
                        System.out.println(user.coffee_tray);*/
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (user.completed_Order()) {
                            String return_CompleteOrder = user.completed_Information();
                            writer.println("Finish order---------------------");
                            writer.println(return_CompleteOrder);
                            print_ChangeStatus();
                            user.tea_tray = 0;
                            user.coffee_tray = 0;
                        }
                    }
                });
                completed_Order.start();


            //completed_Order.start();
            //send message to customer which is printed in terminal
                writer_Barista = new Thread(() -> {
                    Scanner in = new Scanner(System.in);
                    while (true) {
                        String input_Barista = in.nextLine();
                        writer.println(input_Barista);
                    }
                });
                writer_Barista.start();

                System.out.println("New connection; src.Helper.User ID " + user.getId());
                System.out.println("Status---------------------------");
                System.out.println("Client exist " + Barista.number_Client);
                for (Integer key : OrderHandler.users.keySet()) {
                    System.out.println(OrderHandler.users.get(key).getStatus());
                }
                System.out.println("--------------------------------");
                writer.println("SUCCESS");


                while (true) {
                    String line = scanner.nextLine();
                    if (line.contains("order status")) {
                        if (user.coffee_waiting == 0 && user.tea_waiting == 0 && user.coffee_brewing == 0 && user.tea_brewing == 0 && user.tea_tray==0&&user.coffee_tray==0) {
                            writer.println("No order found for " + user.name);
                        } else {
                            String status = user.getStatus();
                            writer.println(status);
                        }
                    } else if (line.contains("coffee") || line.contains("tea")) {
                        String[] substring = line.replace("order ", "").split(" and ");
                        for (String word : substring) {
                            String[] cmd = word.split(" ");
                            int count = Integer.parseInt(cmd[0]);
                            switch (cmd[1]) {
                                case "tea":
                                case "teas":
                                    OrderHandler.orderTea(user, count);
                                    break;
                                case "coffee":
                                case "coffees":
                                    OrderHandler.orderCoffee(user, count);
                                    break;
                            }
                        }
                        int tea_total = user.tea_waiting + user.tea_brewing + user.tea_tray;
                        int coffee_total = user.coffee_waiting + user.coffee_brewing + user.coffee_tray;
                        StringBuffer sb = new StringBuffer("Order received for ");
                        sb.append(user.name).append(" ( ");
                        if (tea_total > 0) {
                            if (tea_total > 1) {
                                sb.append(tea_total + " teas");
                            } else {
                                sb.append(tea_total + " tea");
                            }
                        }
                        if (coffee_total > 0) {
                            if (sb.toString().contains("tea")) {
                                sb.append(" and ");
                            }
                            if (coffee_total > 1) {
                                sb.append(coffee_total + " coffees");
                            } else {
                                sb.append(coffee_total + " coffee");
                            }
                        }
                        sb.append(" )");
                        writer.println(sb.toString());
                        writer.println("Order successfully");
                        print_ChangeStatus();

                    } else {
                        writer.println("Unknown command: " + line);
                        //throw new Exception("Unknown command: " + line);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {e.printStackTrace();
        } finally {
            user.finishHandle();
            Barista.number_Client -=1;
            System.out.println("test D" + Barista.CoffeeGiven);
            System.out.println("src.Helper.User:" + user.getName() + " ID:" + user.getId() + " disconnected.");
            print_ChangeStatus();
        }
    }


    public void print_ChangeStatus(){
        System.out.println("Status---------------------------");
        System.out.println("Client exist " + Barista.number_Client);
        for (Integer key : OrderHandler.users.keySet()) {
            System.out.println(OrderHandler.users.get(key).getStatus());
        }
        System.out.println("--------------------------------");

    }

}




