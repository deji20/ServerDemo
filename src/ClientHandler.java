import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    ServerDemo server;
    Socket socket;
    String name;

    ClientHandler(Socket socket, ServerDemo server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        System.out.println("connected to: " + socket.getPort());
        try(Scanner getInput = new Scanner(socket.getInputStream());) {

            //name prompt
            new PrintWriter(socket.getOutputStream(), true).println("Enter Name:");
            name = getInput.nextLine();
            server.sendToAll(name + " Has Entered The Room", this);

            String s = "";
            //input message loop
            while (true) {
                s = getInput.nextLine();
                if(s.startsWith("#")){
                    server.sendPrivate(s.split(" ")[0].replaceFirst("#", ""), name+": "+ s);
                }else if(s.equalsIgnoreCase("quit")) {
                    break;
                }else server.sendToAll(name + ": " + s, this);
            }
        }
        catch(NoSuchElementException | IOException ns){
            System.out.println("Client: " + name + " Disconnected!");
        }finally {
            try {
                server.sendToAll(name + " Disconnected!", this);
                server.close(this);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
