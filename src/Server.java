import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {

    public static int client_number = 0;
    public static Socket socket = null;
    ArrayList<ClientsThread> ct = new ArrayList<>();
    public static int PORT;
    Scanner scan;
    String str="";

    public Server(int PORT){
        Server.PORT = PORT;
    }

    public static void main(String[] args) {

        Server server = new Server(5000);
        server.run();
        
    }

    public void run(){

        try(ServerSocket serversocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening...");
            while(!str.equals("QUIT")) 
            {
                client_number++;
                socket = serversocket.accept();
                
                System.out.println("Client" + client_number + " is connected");               
                ClientsThread cth = new ClientsThread(socket,client_number,this);
                ct.add(cth);
                cth.start();
                }
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void broadcast(String message, ClientsThread remove) {
        for (ClientsThread clients: ct){
            if (clients != remove) {
                try {
					clients.broadcastMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
    
    public void broadcastServer(String message) {
    	
        for (ClientsThread clients: ct){
                try {
					clients.broadcastMessage("Server: "+message);
				} catch (Exception e) {
					System.out.print(e.getMessage());
				}
        }
    }
}
