package Helpers;

import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client  implements AutoCloseable// i.e. can use in a try-with-resources block
{   final int port = 8888;
    private final Scanner reader;
    private final PrintWriter writer;

    Thread print_thread;


    public Client( String name ) throws Exception {
        Socket socket = new Socket( "localhost", port );            // Connecting to the server,
        reader = new Scanner( socket.getInputStream() );            //   and create objects for communications;
        writer = new PrintWriter( socket.getOutputStream(), true ); // Provides nice 'println' functions (which flush automatically!)
        writer.println(name);// Send customer ID
        String line = reader.nextLine();   // Parse the response
        if (line.trim().compareToIgnoreCase( "success" ) != 0) throw new Exception( line );

        // can receive data from barista anytime
        print_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try{String data = reader.nextLine();
                    data = data.replace("|", "\n");
                    System.out.println(data);}catch (Exception e){

                    }
                }

            }
        });
        print_thread.start();

    }



    public void printMsg(String msg){
        writer.println(msg);
    }

    public void exitSystem() {
        System.exit(0);
    }


    public void call_Function(String msg) {
        if (msg.contains("order")) {
            //order and order status
            printMsg(msg);
        } else if (msg.contains("exit")) {
            //exit
            exitSystem();
        }else {
            printMsg(msg);
        }
    }


    @Override
    public void close() throws Exception {  // Implement AutoCloseable interface!
        reader.close();
        writer.close();
    }

}
