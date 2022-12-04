package Helpers;
import java.util.HashMap;

public class OrderHandler {
    public static HashMap<Integer, User> users = new HashMap<>();


    public static User checkUser(String user_name) {
        for (Integer i : users.keySet()) {
            if (user_name.equals(users.get(i).getName())) {
                return users.get(i);
            }
        }
        return null;
    }
    public static void initialUser(User user){
        users.put(user.getId(),user);
    }
    public static void orderTea(User user, int count){
        user.tea_waiting += count;
    }
    public static void orderCoffee(User user, int count){
        user.coffee_waiting += count;
    }

    public static class tea_maker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    for (Integer key : users.keySet()) {

                        if (Barista.CoffeeGiven > 0 && users.get(key).coffee_waiting > 0) {
                            users.get(key).transferredTea();
                        }

                        if (users.get(key).tea_StartBrewing()) {
                            try {
                                Thread.sleep(30 * 1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            users.get(key).tea_finishBrewing();

                        }
                    }

                }catch (Exception e){System.out.println("Restart making tea");}
            }

        }
    }

    public static class coffee_maker implements Runnable {

        @Override
        public void run() {
            while (true){
                try {
                for (Integer key : users.keySet()){
                    if(Barista.CoffeeGiven > 0 && users.get(key).coffee_waiting >0){
                        users.get(key).transferredCoffee();
                    }

                    if(users.get(key).coffee_StartBrewing()) {

                        try {
                            Thread.sleep(45 * 1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        users.get(key).coffee_finishBrewing();

                    }
                }
                }catch (Exception e){System.out.println("Restart making coffee");}

            }

        }
    }

    public static  void run(){
        tea_maker t = new tea_maker();
        Thread teamaker1 = new Thread(t);
        Thread teamaker2 = new Thread(t);
        teamaker1.start();
        teamaker2.start();

        coffee_maker c = new coffee_maker();
        Thread coffeemaker3 = new Thread(c);
        Thread coffeemaker4 = new Thread(c);
        coffeemaker3.start();
        coffeemaker4.start();
    }

}
