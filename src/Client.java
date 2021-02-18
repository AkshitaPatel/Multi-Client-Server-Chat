
import java.io.*;
import java.net.Socket;


public class Client {

    public static Socket socket = null;
    String HOST;
    int PORT;

    public Client(String HOST, int PORT){
        this.HOST = HOST;
        this.PORT = PORT;
    }

    public static void main(String[] args) throws IOException {

        Client client = new Client("127.0.0.1", 5000);
        client.run();
    }

    public void run() {
        try {
            socket = new Socket(HOST, PORT);
            System.out.println("Connected");
            
            Send sd = new Send(socket, this);
            sd.start();

            Broadcast bd = new Broadcast(socket, this);
            bd.start();
        }catch (IOException e){
            System.out.println(e.getMessage());
            
        }
    }
}


class Send extends Thread {

	Socket socket;
    Client client;
    DataInputStream in;
    String message = null;
    public Send(Socket socket, Client client){
        this.socket = socket;
        this.client = client;

        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        while(true){
            try {
                message = in.readUTF();
                System.out.println(message);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}


class Broadcast extends Thread {

    Socket socket;
    Client client;
    String message = null;
    DataOutputStream dos;
    BufferedReader br;

    public Broadcast(Socket s, Client c){
        this.socket = s;
        this.client = c;
        try{
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        try {
            do {
                message = br.readLine();
                dos.writeUTF(message);
            } while (!message.equals("bye"));

            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
