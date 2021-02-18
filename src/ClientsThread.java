
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientsThread extends Thread {

    Socket socket;
    int client_number;
    Server server;
    DataOutputStream dos;
    DataInputStream in, key;
    BufferedReader br;

    ClientsThread(Socket socket, int number, Server server){
        this.socket = socket;
        this.client_number = number;
        this.server = server;
    }

    @Override
    public void run(){

        String str = null, str2=null;

        try {
            in = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
            dos = new DataOutputStream(socket.getOutputStream());
            do {
                str = in.readUTF();
                System.out.println("Client " + client_number + ": " + str);
                String message = "Client " + client_number + ": " + str;
                server.broadcast(message, this);
                str2 = br.readLine();
                System.out.println(str2);
                server.broadcastServer(str2);
            }while (!str.equals("bye"));
            System.out.println("Client"+client_number+" disconnected.");
            server.ct.remove(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) throws IOException {
        dos.writeUTF(message);
    }
}


