package Helpers;


public class User {
    public String name;
    public int id;
    public static int name_index = 0;

    public int coffee_waiting;
    public int tea_waiting;

    public int coffee_brewing;
    public int tea_brewing;

    public int coffee_tray;
    public int tea_tray;

    public boolean transfer_Tea;
    public boolean transfer_Coffee;

    /*public synchronized void transferTea(){
        if(tea_tray>0){
            Barista.TeaGiven += tea_tray;
            tea_tray = 0;
        }
    }
    public synchronized void transferCoffee(){
        if(coffee_tray>0){
            Barista.CoffeeGiven += coffee_tray;
            coffee_tray = 0;
        }
    }*/

    public synchronized void transferredCoffee(){
        if(Barista.CoffeeGiven >= coffee_waiting){
            int tran = coffee_waiting;
            Barista.CoffeeGiven -= coffee_waiting;
            coffee_tray += coffee_waiting;
            coffee_waiting = 0;
            if (tran >0){
                System.out.println(tran + " coffee currently brewing for "+Barista.name_Transfer+" has been transferred to"+ name+"'s order");}
        }else{
            int tran = Barista.CoffeeGiven;
            coffee_tray += Barista.CoffeeGiven;
            coffee_waiting -=Barista.CoffeeGiven;
            Barista.CoffeeGiven = 0;
            if(tran > 0)
                System.out.println(tran + " coffee currently brewing for " + Barista.name_Transfer + " has been transferred to" + name + "'s order");

        }
    }

    public synchronized void transferredTea(){
        if(Barista.TeaGiven >= tea_waiting){
            int tran = tea_waiting;
            Barista.TeaGiven -= tea_waiting;
            tea_tray += tea_waiting;
            tea_waiting = 0;
            if(tran > 0)
                System.out.println(tran + " tea currently brewing for "+Barista.name_Transfer+" has been transferred to"+ name+"'s order");
        }else{
            int tran = Barista.TeaGiven;
            tea_tray += Barista.TeaGiven;
            tea_waiting -=Barista.TeaGiven;
            Barista.TeaGiven = 0;
            if(tran > 0)
                System.out.println(tran + " tea currently brewing for "+Barista.name_Transfer+" has been transferred to"+ name+"'s order");

        }
    }
    public String getName(){
        return name;
    }


    public int getId(){
        return name_index;
    }

    public User(String name){
        this.name = name;
        name_index += 1;
        id = name_index;
    }

    public synchronized boolean tea_StartBrewing(){
        if (tea_waiting -1 >= 0) {
            tea_waiting -= 1;
            tea_brewing += 1;
            return  true;
        }
        return false;
    }

    public synchronized void tea_finishBrewing(){
        if(tea_brewing >0){
        tea_brewing -=1;
        tea_tray += 1;}
    }

    public synchronized boolean coffee_StartBrewing(){
        if (coffee_waiting > 0) {
            coffee_waiting -= 1;
            coffee_brewing += 1;
            return  true;
        }
        return false;
    }

    public synchronized void coffee_finishBrewing(){
        if(coffee_brewing >0){
        coffee_brewing -=1;
        coffee_tray += 1;}
    }

    public boolean completed_Order(){
        if(tea_waiting == 0 && tea_brewing == 0 && coffee_waiting == 0 && coffee_brewing == 0){
            if(tea_tray > 0 || coffee_tray> 0){
                return true;
            }
        }
        return false;
    }

    public String completed_Information(){
        StringBuffer sb = new StringBuffer("Order delivered to ");
        sb.append(name).append(" ( ");

        if(tea_tray > 1) {
            sb.append(tea_tray + " teas");
        }else{sb.append(tea_tray + " tea");}

        if (coffee_tray > 0) {
            if (sb.toString().contains("tea")){
                sb.append(" and ");
            }
            if (coffee_tray >1){
                sb.append(coffee_tray+" coffees");
            }else{sb.append(coffee_tray + " coffee");}
        }
        sb.append(" )");
        return sb.toString();
    }



    public String getStatus(){
        StringBuffer sb1 =  new StringBuffer("Order status for " + name + ":"+ "\r\n");


        StringBuffer sb2 =  new StringBuffer("- ");

        if(coffee_waiting > 1){
            sb2.append(coffee_waiting+" coffees");
        }else{
            sb2.append(coffee_waiting+" coffee");
        }
        if(tea_waiting > 1){
            sb2.append(" and "+tea_waiting+" teas ");
        }else{
            sb2.append(" and "+tea_waiting+" tea ");
        }
        sb2.append("in the waiting area"+"\r\n");

        StringBuffer sb3 =  new StringBuffer("- ");
        if(coffee_brewing > 1){
            sb3.append(coffee_brewing+" coffees");
        }else{
            sb3.append(coffee_brewing+" coffee");
        }
        if(tea_brewing > 1){
            sb3.append(" and "+tea_brewing+" teas ");
        }else{
            sb3.append(" and "+tea_brewing+" tea ");
        }
        sb3.append("currently being prepared"+"\r\n");

        StringBuffer sb4 =  new StringBuffer("- ");

        if(coffee_tray > 1){
            sb4.append(coffee_tray+" coffees");
        }else{
            sb4.append(coffee_tray+" coffee");
        }
        if(tea_tray > 1){
            sb4.append(" and "+tea_tray+" teas ");
        }else{
            sb4.append(" and "+tea_tray+" tea ");
        }
        sb4.append("currently in the tray"+"\r\n");

        /*
        StringBuffer sb2 =  new StringBuffer();
        if(coffee_waiting > 0){
           if(coffee_waiting ==1){
                sb2.append
           }
           sb2.append
        }
         */
        String status = String.valueOf(sb1.append(sb2).append(sb3).append(sb4));

        return status;
    }

    public void finishHandle(){
        coffee_waiting = 0;
        tea_waiting = 0;
        coffee_brewing = 0;
        tea_brewing = 0;
        if(coffee_tray >0 || tea_tray >0){
            Barista.name_Transfer = name;
            Barista.CoffeeGiven += coffee_tray;
            Barista.TeaGiven += tea_tray;
            coffee_tray = 0;
            tea_tray = 0;
        }
        transfer_Tea = true;
        transfer_Coffee = true;

    }


}
