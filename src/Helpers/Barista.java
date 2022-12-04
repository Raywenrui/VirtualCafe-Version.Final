package Helpers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Barista
{   private final static int port = 8888;
    public static int number_Client = 0;
    public static int TeaGiven = 0;
    public static int CoffeeGiven = 0;
    public static String name_Transfer;

    public static void main(String[] args)
    {
        OrderHandler.run();
        RunServer();
    }

    private static void RunServer()
    {   ServerSocket serverSocket = null;   // passive socket, used for 'listening'
        try
        {   serverSocket = new ServerSocket( port );  // bind port and start listening
            System.out.println( "Waiting for incoming connections..." );
            while (true)
            {   Socket socket = serverSocket.accept();  // accept incoming connections (blocks until it does!)
                new Thread( new ClientHandler(socket) ).start();
                number_Client +=0.5;
            }
        }
        catch (IOException e) { e.printStackTrace(); }
    }




}
