import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerDemo {
    static Set<ClientHandler> clients;

    public static void main(String[] args) {
        new ServerDemo();
    }

    ServerDemo(){
        clients = new HashSet<>();
        try (ServerSocket serverSocket = new ServerSocket(1553)) {
            System.out.println("connecting to client: ");
            while(true) {
                ClientHandler client = new ClientHandler(serverSocket.accept(), this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String msg, ClientHandler client) throws IOException{
        for (ClientHandler c: clients) {
            if (c != client) {
                new PrintWriter(c.getSocket().getOutputStream(), true).println(msg);
            }
        }
    }

    public void sendPrivate(String name, String msg) throws IOException{
        for (ClientHandler client: clients){
            if(client.getName().equals(name)){
                new PrintWriter(client.getSocket().getOutputStream(), true).println(msg);
            }
        }
    }

    public void close(ClientHandler client){
        clients.remove(client);
    }

}
